package main.java.com.expense.tracker.cli.utils;

import java.util.function.Function;

public class Result<T> {

    private final T instance;
    private final String[] _errors;

    private Result(T instance, String[] _errors) {
        this.instance = instance;
        this._errors = _errors;
    }

    public static <T> Result<T> tryParse(String value, Function<String,T> fn) {
        try {
            return new Result<T>(fn.apply(value), null);
        } catch (Exception e) {
            String error = e.getMessage();
            String[] errors = {error};
            return new Result<T>(null, errors);
        }
    }

    public T getInstance() {
        return instance;
    }

    public String[] getErrors() {
        return _errors;
    }

    public boolean isFailure() {
        return _errors != null && _errors.length > 0;
    }

    public boolean isOk() {
        return !isFailure() && instance != null;
    }

    public boolean isNull() {
        return !isFailure() && instance == null;
    }

    public static <T> Result<T> ok(T value) {
        return new Result<T>(value, null);
    }

    public static <T> Result<T> fail(String[] errors) {
        return new Result<T>(null, errors);
    }

    public static <T> Result<T> fail(String error) {
        return new Result<T>(null, new String[]{error});
    }

}
