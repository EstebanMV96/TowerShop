/**
 * Vista para los leads del CRM
 */
package com.wexu.huckster.control.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wexu.huckster.R;
import com.wexu.huckster.modelo.Chat;
import com.wexu.huckster.modelo.Huckster;

public class ConversationsFragment extends Fragment {

    private ListView chatsList;
    private ConversationsAdapter chatsAdapter;
    private Huckster principal;

    public ConversationsFragment() {
        // Required empty public constructor
    }

    public static ConversationsFragment newInstance(/*parámetros*/) {
        ConversationsFragment fragment = new ConversationsFragment();
        // Setup parámetros
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            principal=(Huckster) getArguments().getSerializable("modelo");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_conversations, container, false);

        // Instancia del ListView.
        chatsList = (ListView) root.findViewById(R.id.leads_list);

        // Inicializar el adaptador con la fuente de datos.
        Log.d("ERROR",principal.darUsuario().getChats()+"");
        chatsAdapter = new ConversationsAdapter(getActivity(), principal.darUsuario().getChats());

        //Relacionando la lista con el adaptador
        chatsList.setAdapter(chatsAdapter);

        chatsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Chat currentLead = chatsAdapter.getItem(position);
                currentLead.armarConversacion();

                Intent i= new Intent(getActivity(), ChatActivity.class);
                principal.setPerdido(true);
                Log.d("PERDIDO FRAG",principal.darPerdido()+"");
                i.putExtra("chat",currentLead );
                i.putExtra("modelo",principal);
                
                startActivity(i);

            }
        });

        return root;
    }

    public void refresh()
    {
        chatsAdapter.notifyDataSetChanged();
    }


}