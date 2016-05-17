package com.automotive.hhi.mileagetracker.presenter;


/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
