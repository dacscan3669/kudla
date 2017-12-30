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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactItem {

    public String contactType;
    public String title; //This is also Theatre name
    public String speciality; //This is also Movie name
    public String timings;
    public String address;
    public String contact;
    public String distance;
    public String email;
    public String area;
    public String fees;
    public String status;
    public String createdDate;
    public String createdBy;


    public String cast;
    public String lang; //language and duration
    public String day0, day1, day2, day3, day4, day5, day6;

    public interface DataListener {
        void newDataReceived(ArrayList<ContactItem> itemList);
    }

    public ContactItem () {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }
    public String getTitle() { return title; }
    public String getSpeciality() { return speciality; }
    public String getTimings() { return timings; }
    public String getAddress() { return address; }
    public String getContact() { return contact; }
    public String getDistance() { return distance; }
    public String getContactType() { return contactType; }
    public String getEmail() { return email; }
    public String getArea() { return area; }
    public String getFees() { return fees; }
    public String getStatus() { return status; }
    public String getCreatedDate() { return createdDate; }
    public String getCreatedBy() { return createdBy; }

    public String getCast() { return cast; }
    public String getLang() { return lang; }
    public String getDay0() { return day0; }
    public String getDay1() { return day1; }
    public String getDay2() { return day2; }
    public String getDay3() { return day3; }
    public String getDay4() { return day4; }
    public String getDay5() { return day5; }
    public String getDay6() { return day6; }

    public static void getContactsFromDB(final DataListener dataListener, String DBReference, String contactType){
        final ArrayList<ContactItem> itemList = new ArrayList<>();
        final DatabaseReference mDatabase;
        final ContactItem item = new ContactItem();
        final String type = contactType;

        mDatabase = FirebaseDatabase.getInstance().getReference(DBReference);
        Query myQuery = mDatabase.orderByChild("title");

        //mDatabase.addValueEventListener(new ValueEventListener() {
        //mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("There are " + snapshot.getChildrenCount() + " records");

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ContactItem post = postSnapshot.getValue(ContactItem.class);

                    ContactItem item = new ContactItem();

                    item.status = post.getStatus();
                    item.title = post.getTitle();
                    item.speciality = post.getSpeciality();

                    if (type == "Movies") {
                        item.cast = post.getCast();
                        item.lang = post.getLang();
                        item.day0 = post.getDay0();
                        item.day1 = post.getDay1();
                        item.day2 = post.getDay2();
                        item.day3 = post.getDay3();
                        item.day4 = post.getDay4();
                        item.day5 = post.getDay5();
                        item.day6 = post.getDay6();
                    } else {
                        item.timings = post.getTimings();
                        item.address = post.getAddress();
                        item.contact = post.getContact();
                        item.distance = post.getDistance();
                    }

                    if ( item.status.equals("ON")) {
                        itemList.add(item); }
                }
                // Transaction complete, sending to listener
                dataListener.newDataReceived(itemList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

        });
    }

    public ContactItem(String contactType, String title, String speciality, String contact, String email, String address, String area,
                        String distance, String fees, String timings, String status, String createdDate, String createdBy) {
        this.contactType = contactType;
        this.title = title;
        this.speciality = speciality;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.area = area;
        this.distance = distance;
        this.fees = fees;
        this.timings = timings;
        this.status = status;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("speciality", speciality);
        result.put("contact", contact);
        result.put("email", email);
        result.put("address", address);
        result.put("area", area);
        result.put("distance", distance);
        result.put("fees", fees);
        result.put("timings", timings);
        result.put("status", status);
        result.put("createdDate", createdDate);
        result.put("createdBy", createdBy);
        return result;
    }

    public void InsertContactIntoDB(String contactType, String title, String speciality, String contact, String email, String address, String area,
                                    String distance, String fees, String timings, String status, String createdDate, String createdBy) {

        ContactItem post = new ContactItem(contactType, title, speciality, contact, email, address, area,
                                                    distance, fees, timings, status, createdDate, createdBy);
        //String DBReference = "contacts";
        String DBReference = "cities/Mangalore";

        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(DBReference);

        String key = mDatabase.child(contactType).push().getKey();

        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + contactType + "/" + key, postValues);
        //childUpdates.put("/Cars/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        System.out.println("key is ..." + key);
    }

}

