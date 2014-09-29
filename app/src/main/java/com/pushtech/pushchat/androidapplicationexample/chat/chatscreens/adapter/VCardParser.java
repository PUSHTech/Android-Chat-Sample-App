package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.Email;
import ezvcard.property.Telephone;

/**
 * Created by goda87 on 2/09/14.
 */
public class VCardParser {
    public static void parseVCard(Context context, String vCardString) {
        Toast.makeText(context, "Saving contact...", Toast.LENGTH_SHORT).show();

        VCard vCard = Ezvcard.parse(vCardString).first();

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        // Name
        String name = (vCard.getStructuredName().getGiven() != null ? vCard.getStructuredName()
                .getGiven() : "") + " " +
                (vCard.getStructuredName().getFamily() != null ? vCard.getStructuredName()
                        .getFamily() : "");
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        name).build());


        // Phones
        for (Telephone phone : vCard.getTelephoneNumbers()) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.CommonDataKinds.Phone.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getText())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }


        // Email
        for (Email email : vCard.getEmails()) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.CommonDataKinds.Email.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email.getValue())
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                            ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }


        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);

            Toast.makeText(context,
                    "Contact Saved.." + name, Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception e) {

            Toast.makeText(context, "Contact could not be saved", Toast.LENGTH_SHORT).show();
        }
    }
}
