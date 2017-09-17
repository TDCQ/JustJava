package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 *
 * package com.example.android.justjava;
 */

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.id.message;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    final int maxCup = 100;
    final int minCup = 1;
    int quantity = 1;
    int price = 5;
    boolean hasWhippedCream = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.v("MainActivity", "checbox=" + hasWhippedCream + '\n');  // 可以用这个来记录日志，不过用完就要注释掉。因为android产生的日志特别多，你不注释掉影响你找问题
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        boolean hasWhippedCream;
        boolean hasChocolate;
        String customerName;

        // Get customer name
        EditText editText = (EditText) findViewById(R.id.customer_name);
        customerName = editText.getText().toString();

        // Figure out if the user wants whipped cream topping
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        hasWhippedCream = whippedCreamCheckBox.isChecked();

        // Figure out if the customer wants chocolate
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.cholocate_checkbox);
        hasChocolate = chocolateCheckBox.isChecked();

        String printMessage = createOrderSummary(customerName,  hasWhippedCream, hasChocolate, quantity, price);

        // use a intent to launch a email app
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SENDTO);
//        sendIntent.addCategory(Intent.CATEGORY_APP_EMAIL);  // 添加了这一行就找不到可以发邮件的app了！！
        sendIntent.setType("text/plain");
        sendIntent.setData(Uri.parse("mailto:"));
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "" + R.string.order_summary_email_subject + customerName);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);

        // Verify that the intent will resolve to an activity
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendIntent);
        }

    }

    @NonNull
    private String createOrderSummary(String customerName,  boolean hasWhippedCream,boolean hasChocolate, int quantity, int price) {
        // Create the text message with a string
        String message = "Name: " + customerName;
        message += "\nAdd Whipped Cream? " + hasWhippedCream;
        message += "\nAdd Cholocate? " + hasChocolate;
        message += "\nQuantity: " + quantity;
        message += "\nTotal: $" + calculatePrice(quantity, price,hasWhippedCream, hasChocolate);
        message += "\nThank you !";
        return message;
    }

    public void increment(View view) {
        if(quantity >= maxCup){
            toast();
            return;
        }
        quantity = quantity + 1;
        display(quantity);
    }

    public void decrement(View view) {
        if (quantity <= minCup){
            Toast.makeText(this, "Cups of Coffee can't less than 1", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        display(quantity);
    }

    /**
     *  custom toast
     */
    private void toast(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));
        TextView text = layout.findViewById(R.id.text);
        if (quantity >= maxCup ){
            text.setText("Cups of coffee can't more than 100 ");
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }
    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    private int calculatePrice(int quantity, int price, boolean hasWhippedCream, boolean hasChocolate) {
        int ext = 0;
        if (hasWhippedCream) {
            ext += 1;
        }
        if (hasChocolate) {
            ext += 2;
        }
        return quantity * (price + ext);

    }
    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(message);
    }

}