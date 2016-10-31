package by.vshkl.mvp.domain.common;

import by.vshkl.mvp.model.ResponseWrapper;
import io.reactivex.functions.Function;

public class ResponseMappingFunc<R> implements Function<ResponseWrapper<R>, R> {

    @Override
    public R apply(ResponseWrapper<R> rResponseWrapper) throws Exception {
        if (rResponseWrapper == null) {
            return null;
        }
        return rResponseWrapper.body;
    }
}
