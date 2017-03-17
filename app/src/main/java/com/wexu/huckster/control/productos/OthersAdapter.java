package com.wexu.huckster.control.productos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wexu.huckster.R;
import com.wexu.huckster.control.vendedor.PublishActivity;
import com.wexu.huckster.modelo.Producto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by Juliana on 28/11/2016.
 */

public class OthersAdapter extends RecyclerView.Adapter<OthersAdapter.ProductViewHolder>{

    private static List<Producto> items;

    private static AdapterView.OnItemClickListener onItemClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case

        public ImageView imagen;
        public TextView nombre;
        public TextView precio;
        private TextView descripcion;
        private TextView contacto;


        public TextView mTextView;
        public ProductViewHolder(View v) {

            super(v);

            imagen = (ImageView) v.findViewById(R.id.imgProdcut);
            nombre = (TextView) v.findViewById(R.id.txtProductName);
            precio = (TextView) v.findViewById(R.id.txtPrice);
            descripcion = (TextView) v.findViewById(R.id.txtProductDescription);
            contacto= (TextView) v.findViewById(R.id.contacto);
            LinearLayout ly= (LinearLayout)v.findViewById(R.id.card_product);
            ly.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Producto p=items.get(getAdapterPosition());
            System.out.println("seleccionado: "+p.getNombre());

        }
    }

    public ProductViewHolder getCustomHolder(View v) {
        return new ProductViewHolder(v) {
            @Override
            public void onClick(View v) {
                Producto p=items.get(this.getAdapterPosition());
                System.out.println("Artículo seleccionado: "+p.getNombre()+p.getPrecio());
            }
        };
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OthersAdapter(List<Producto> myDataset) {
        items = myDataset;

    }

    // Create new views (invoked by the layout manager)

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card, viewGroup, false);

        return new ProductViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ProductViewHolder viewHolder, int i) {


        Bitmap bMap = decodeFile(new File(PublishActivity.RUTA_FOTOS + items.get(i).getNombreImagen() ),256,256);
        viewHolder.imagen.setImageBitmap(bMap);
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.precio.setText("$"+String.valueOf(items.get(i).getPrecio()));
        viewHolder.descripcion.setText(items.get(i).getDescripcion());
        viewHolder.contacto.setText("Contactar a "+items.get(i).getNombreVendedor());
        System.out.println("NIIICKKKK"+items.get(i).getNickVendedor());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
    public Producto getItem(int position){return items.get(position);}


    public static Bitmap decodeFile(File f, int WIDTH, int HIGHT){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH=WIDTH;
            final int REQUIRED_HIGHT=HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

}
