/*
 * Copyright (c) 2016 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ferainc.kudla.itemclasses;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProductItem implements Serializable {

    public String category, subCategory;
    public String title, description, price, year, kms, mileage, colour, name, phone, email, address;
    public String fuel, condition, aircon;
    public String imageURL0, imageURL1, imageURL2, imageURL3, imageURL4;
    public String[] imageURL;
    public String createdDate, createdDateMs;
    public String createdBy;
    //public String imageURLs;

    public interface DataListener {
        void newDataReceived(ArrayList<ProductItem> itemList);
    }

    public ProductItem () {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }
    public String getCategory() { return category; }
    public String getSubCategory() { return subCategory; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCondition() { return condition; }
    public String getPrice() { return price; }
    public String getYear() { return year; }
    public String getKms() { return kms; }
    public String getMileage() { return mileage; }
    public String getColour() { return colour; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getFuel() { return fuel; }
    public String getAircon() { return aircon; }
    public String getImageURL0() { return imageURL0; }
    public String getImageURL1() { return imageURL1; }
    public String getImageURL2() { return imageURL2; }
    public String getImageURL3() { return imageURL3; }
    public String getImageURL4() { return imageURL4; }
    public String getCreatedDate() { return createdDate; }
    public String getCreatedDateMs() { return createdDateMs; }
    public String getCreatedBy() { return createdBy; }

    public static void getProductsFromDB(final DataListener dataListener, String DBReference){
        final ArrayList<ProductItem> itemList = new ArrayList<>();
        final DatabaseReference mDatabase;
        final ProductItem item = new ProductItem();

        mDatabase = FirebaseDatabase.getInstance().getReference(DBReference);
        Query myQuery = mDatabase.orderByChild("createdDateMs");

        //mDatabase.addValueEventListener(new ValueEventListener() {
        //mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ProductItem post = postSnapshot.getValue(ProductItem.class);

                    ProductItem item = new ProductItem();

                    item.category = post.getCategory();
                    item.subCategory = post.getSubCategory();
                    item.title = post.getTitle();
                    item.description = post.getDescription();
                    item.condition = post.getCondition();
                    item.price = post.getPrice();
                    item.year = post.getYear();
                    item.kms = post.getKms();
                    item.mileage = post.getMileage();
                    item.fuel = post.getFuel();
                    item.colour = post.getColour();
                    item.aircon = post.getAircon();
                    item.name = post.getName();
                    item.phone = post.getPhone();
                    item.email = post.getEmail();
                    item.address = post.getAddress();
                    item.imageURL0 = post.getImageURL0();
                    item.imageURL1 = post.getImageURL1();
                    item.imageURL2 = post.getImageURL2();
                    item.imageURL3 = post.getImageURL3();
                    item.imageURL4 = post.getImageURL4();

                    itemList.add(item);
                }
                // Transaction complete, sending to listener
                Collections.reverse(itemList);
                dataListener.newDataReceived(itemList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

        });

    }

    public ProductItem(String category, String subCategory, String title, String description, String condition, String price,
                       String year, String kms, String mileage, String colour, String name, String phone, String email, String address,
                        String fuel, String aircon, String[] imageURL, String createdDate, String createdDateMs, String createdBy) {
        this.category = category;
        this.subCategory = subCategory;
        this.title = title;
        this.description = description;
        this.condition = condition;
        this.price = price;
        this.year = year;
        this.kms = kms;
        this.mileage = mileage;
        this.colour = colour;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.fuel = fuel;
        this.aircon = aircon;
        this.imageURL = imageURL;
        this.createdDate = createdDate;
        this.createdDateMs = createdDateMs;
        this.createdBy = createdBy;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("category", category);
        result.put("subCategory", subCategory);
        result.put("title", title);
        result.put("description", description);
        result.put("condition", condition);
        result.put("price", price);

        if ( category.equals("Vehicles")) {
            result.put("year", year);
            result.put("kms", kms);
            result.put("mileage", mileage);
            result.put("colour", colour);
            result.put("fuel", fuel);
            result.put("aircon", aircon);

        }
        result.put("name", name);
        result.put("phone", phone);
        result.put("email", email);
        result.put("address", address);
        result.put("imageURL0", imageURL[0]);
        result.put("imageURL1", imageURL[1]);
        result.put("imageURL2", imageURL[2]);
        result.put("imageURL3", imageURL[3]);
        result.put("imageURL4", imageURL[4]);
        result.put("createdDate", createdDate);
        result.put("createdDateMs", createdDateMs);
        result.put("createdBy", createdBy);
        return result;
    }

    public void InsertProductIntoDB(String category, String subCategory, String title, String description, String condition, String price,
                             String year, String kms, String mileage, String colour, String name, String phone, String email,
                 String address, String fuelType, String aircon, String[] imageURL, String createdDate, String createdDateMs, String createdBy) {

        ProductItem post = new ProductItem(category, subCategory, title, description, condition, price,
                year, kms, mileage, colour, name, phone,
                email, address, fuelType, aircon, imageURL, createdDate, createdDateMs, createdBy);

        final DatabaseReference mDatabase;
        final String DBReference = "products/" + category + "/";
        mDatabase = FirebaseDatabase.getInstance().getReference(DBReference);

        String key = mDatabase.child(subCategory).push().getKey();

        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + subCategory + "/" + key, postValues);
        //childUpdates.put("/Cars/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        System.out.println("key is ..." + key);
    }

}
