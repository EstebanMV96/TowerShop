package com.wexu.huckster.control.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wexu.huckster.R;
import com.wexu.huckster.modelo.Chat;

import java.util.List;

/**
 * Adaptador de leads
 */
public class ConversationsAdapter extends ArrayAdapter<Chat> {
    public ConversationsAdapter(Context context, List<Chat> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        // ¿Ya se infló este view?
        if (null == convertView) {
            //Si no existe, entonces inflarlo con image_list_view.xml
            convertView = inflater.inflate(
                    R.layout.conversation_item,
                    parent,
                    false);

            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.txtPersonName);
            holder.last = (TextView) convertView.findViewById(R.id.txtLast);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lead actual.
        Chat C = getItem(position);

        // Setup.
        holder.name.setText(C.darNombreDestinatario());
        holder.last.setText(C.getLastMessage().getMensaje());

        return convertView;
    }

    static class ViewHolder {

        TextView name;
        TextView last;
    }
}