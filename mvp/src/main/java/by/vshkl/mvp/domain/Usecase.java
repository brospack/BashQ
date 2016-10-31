package by.vshkl.mvp.domain;

import io.reactivex.Observable;

public interface Usecase<T> {

    Observable<T> execute();
}
