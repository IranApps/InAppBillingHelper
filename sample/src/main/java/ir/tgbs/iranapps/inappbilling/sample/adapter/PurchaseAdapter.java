package ir.tgbs.iranapps.inappbilling.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.tgbs.iranapps.billing.helper.model.PurchaseData;
import ir.tgbs.iranapps.inappbilling.sample.R;


public class PurchaseAdapter extends BaseAdapter {
    private Context context;
    private List<PurchaseData> lstPurchase;

    public PurchaseAdapter(Context context, ArrayList<PurchaseData> list) {
        this.context = context;
        this.lstPurchase = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_purchase, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(context.getString(R.string.sku_id) + " " + lstPurchase.get(position).sku);

        return convertView;
    }


    @Override
    public int getCount() {
        return lstPurchase.size();
    }

    @Override
    public Object getItem(int position) {
        return lstPurchase.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView tv_title;
    }


}

