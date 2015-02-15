package ir.tgbs.iranapps.inappbilling.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.tgbs.iranapps.billing.helper.model.Product;
import ir.tgbs.iranapps.inappbilling.sample.R;


public class SkuAdapter extends BaseAdapter {
    private Context context;
    private List<Product> products;

    public SkuAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.products = list;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sku, parent, false);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(context.getString(R.string.sku_name) + " " + products.get(position).title);
        holder.tv_price.setText(context.getString(R.string.sku_price) + " " + products.get(position).price + " " + context.getString(R.string.toman));
        return convertView;
    }

    private class ViewHolder {
        TextView tv_title;
        TextView tv_price;
    }
}

