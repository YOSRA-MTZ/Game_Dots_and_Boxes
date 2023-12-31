package com.dotsandboxes.utils;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AdsWrapper {

    private static final String TAG = "Ads";

    @Nullable
    private Context mContext;

    @Nullable
    private Location mLocation;

    private int mGender = -1;

    @Nullable
    private String[] mTestDeviceIds;

    @Nullable
    private Date mBirthdayDate;

    private AdsWrapper() {
        //no direct instances. use builder instead.
    }

    private AdsWrapper(Context context, @Nullable String[] testDeviceIds, @Nullable Location location, int gender, @Nullable Date birthdayDate) {
        this.mContext = context.getApplicationContext();
        this.mLocation = location;
        this.mGender = gender;
        this.mTestDeviceIds = testDeviceIds;
        this.mBirthdayDate = birthdayDate;
    }

    // Vous pouvez conserver ou modifier cette classe interne selon vos besoins.
    public static class Builder {

        private Context context;
        private String[] testDeviceIds;
        private Location targetedLocation;
        private Date birthdayDate;
        private int targetedGender = -1; // Ajustez cette partie selon vos besoins.

        public Builder with(@NonNull Context context) {
            this.context = context;
            return this;
        }

        public Builder addTestDeviceIds(@NonNull String[] testDeviceIds) {
            this.testDeviceIds = testDeviceIds;
            return this;
        }

        public Builder targetGender(int targetedGender) {
            this.targetedGender = targetedGender;
            return this;
        }

        public Builder targetLocation(@NonNull Location targetedLocation) {
            this.targetedLocation = targetedLocation;
            return this;
        }

        public Builder targetAge(@NonNull Date birthdayDate) {
            this.birthdayDate = birthdayDate;
            return this;
        }

        public AdsWrapper build() {
            if (context == null)
                throw new IllegalStateException("context is null. use with() and pass context");
            return new AdsWrapper(context, testDeviceIds, targetedLocation, targetedGender, birthdayDate);
        }
    }
}
