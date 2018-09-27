package com.polsec.pyrky.helper;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.polsec.pyrky.pojo.Booking;

import java.util.List;

public class RecyclerDiffCallBack extends DiffUtil.Callback {

    private final List<Booking> mOldBookingList;
    private final List<Booking> mNewBookingList;

    public RecyclerDiffCallBack(List<Booking> mOldBookingList, List<Booking> mNewBookingList) {
        this.mOldBookingList = mOldBookingList;
        this.mNewBookingList = mNewBookingList;
    }

    @Override
    public int getOldListSize() {
        return mOldBookingList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewBookingList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

        return mOldBookingList.get(oldItemPosition).getDestLat().equals(mNewBookingList.get(newItemPosition).getDestLat());

    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Booking oldBooking = mOldBookingList.get(oldItemPosition);
        final Booking newBooking = mNewBookingList.get(newItemPosition);

        return oldBooking.getParkingSpaceRating() == newBooking.getParkingSpaceRating();

    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);

    }
}
