package ir.tgbs.iranapps.inappbilling.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ir.tgbs.iranapps.inappbilling.sample.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bSku = (Button) findViewById(R.id.b_sku);
        bSku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shows sku details
                Intent intent = new Intent(getApplicationContext(), SkuActivity.class);
                startActivity(intent);
            }
        });


        Button bPurchase = (Button) findViewById(R.id.b_purchase);
        bPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shows purchases detail
                Intent intent = new Intent(getApplicationContext(), PurchasesActivity.class);
                startActivity(intent);
            }
        });

    }
}
