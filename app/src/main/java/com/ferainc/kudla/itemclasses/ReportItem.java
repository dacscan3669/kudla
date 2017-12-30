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

public class ReportItem {

    public String reportType;
    public String description;
    public String email;
    public String createdDate;
    public String createdBy;

    public interface DataListener {
        void newDataReceived(ArrayList<ContactItem> itemList);
    }

    public ReportItem () {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }
    public String getContactType() { return reportType; }
    public String getDescription() { return description; }
    public String getEmail() { return email; }
    public String getCreatedDate() { return createdDate; }
    public String getCreatedBy() { return createdBy; }

    public ReportItem(String reportType, String description, String email, String createdDate, String createdBy) {
        this.reportType = reportType;
        this.description = description;
        this.email = email;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("description", description);
        result.put("email", email);
        result.put("createdDate", createdDate);
        result.put("createdBy", createdBy);
        return result;
    }

    public void InsertReportIntoDB(String reportType, String description, String email, String createdDate, String createdBy) {

        ReportItem post = new ReportItem(reportType, description, email, createdDate, createdBy);
        String DBReference = "reportsKudla";

        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(DBReference);

        String key = mDatabase.child(reportType).push().getKey();

        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + reportType + "/" + key, postValues);
        //childUpdates.put("/Cars/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        System.out.println("key is ..." + key);
    }

}

