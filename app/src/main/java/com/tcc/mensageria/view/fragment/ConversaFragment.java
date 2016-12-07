package com.tcc.mensageria.view.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tcc.mensageria.R;
import com.tcc.mensageria.model.MensageriaContract;
import com.tcc.mensageria.network.SocketConversa;
import com.tcc.mensageria.view.adapter.MensagensAdapter;


public class ConversaFragment extends Fragment implements MensagensAdapter.ItemClickCallback, LoaderManager.LoaderCallbacks<Cursor> {

    final String TAG = this.getClass().getSimpleName();
    final int LOADER_ID = 1;
    public static String BUNDLE_ID_CONVERSA = "BUNDLE_ID_CONVERSA";


    RecyclerView mRecyclerView;
    MensagensAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    TextView mViewVazia;

    int mIdConversa;
    SocketConversa mSocketConversa;

    private static final int TYPING_TIMER_LENGTH = 600;
    private EditText mInputMessageView;
    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mIdConversa = getArguments().getInt(BUNDLE_ID_CONVERSA);
        mSocketConversa = new SocketConversa(getActivity(), mIdConversa);

        getLoaderManager().initLoader(LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversa, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lista_mensagens);
        mViewVazia = (TextView) rootView.findViewById(R.id.view_vazia);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MensagensAdapter(null, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickCallback(this);

        mInputMessageView = (EditText) rootView.findViewById(R.id.mensagem_input);
        mInputMessageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.send || id == EditorInfo.IME_NULL) {
                    enviarMensagem();
                    return true;
                }
                return false;
            }
        });
        mInputMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mSocketConversa.getSocket().connected()) return;

                if (!mTyping) {
                    mTyping = true;
                    mSocketConversa.getSocket().emit("typing");
                }

                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ImageButton sendButton = (ImageButton) rootView.findViewById(R.id.botao_enviar);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensagem();
            }
        });

        return rootView;
    }

    private void enviarMensagem() {
        if (mSocketConversa.tentarEnvio(mInputMessageView.getText().toString().trim())) {
            mInputMessageView.setText("");
        } else {
            mInputMessageView.requestFocus();
        }
    }


    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;
            mTyping = false;
            mSocketConversa.getSocket().emit("stop typing");
        }
    };

    /**
     * Metodo para verificar se o adapter tem dados para popular a view
     * caso nao haja dados uma mensagem é mostrada
     *
     * @return false se a lista puder ser populada e true se nao puder
     */
    private boolean listaEstaVazia() {
        if (mAdapter.getCursor().moveToFirst()) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mViewVazia.setVisibility(View.GONE);
            return false;
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mViewVazia.setVisibility(View.VISIBLE);
            return true;
        }
    }

    @Override
    public void onItemClick(int p) {

    }

    @Override
    public void onSecondaryIconClick(int p) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] COLUNAS = {
                MensageriaContract.Conversas.NOME_TABELA + "." + MensageriaContract.Mensagens._ID,
                MensageriaContract.Mensagens.COLUNA_DATA_ENVIO,
                MensageriaContract.Mensagens.COLUNA_CONTEUDO,
                MensageriaContract.Autores.COLUNA_NOME,
                MensageriaContract.Conversas.COLUNA_TITULO
        };

        //conversas._id = mensagens.fk_conversa and conversas._id = x and mensagens.fk_autor = autores._id group by mensagens._id
        String selection = MensageriaContract.Conversas.NOME_TABELA + "." +
                MensageriaContract.Conversas._ID +
                " = " + MensageriaContract.Mensagens.COLUNA_FK_CONVERSA +

                " AND " + MensageriaContract.Conversas.NOME_TABELA + "." +
                MensageriaContract.Conversas._ID + " = " + mIdConversa +

                " AND " + MensageriaContract.Mensagens.COLUNA_FK_AUTOR + " = " +
                MensageriaContract.Autores.NOME_TABELA + "." + MensageriaContract.Autores._ID +
                ") GROUP BY (" + MensageriaContract.Mensagens.NOME_TABELA + "."
                + MensageriaContract.Mensagens._ID;

        String orderBy = MensageriaContract.Mensagens.COLUNA_DATA_ENVIO + " ASC";

        return new CursorLoader(getActivity(),
                MensageriaContract.Conversas.buildConversacomAutorEMensagem(),
                COLUNAS,
                selection,
                null,
                orderBy
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        listaEstaVazia();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}