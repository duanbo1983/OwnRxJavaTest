package test;

import rx.subjects.BehaviorSubject;

public class DataSource {

    private String mUuid = "012";

    private final BehaviorSubject<String> mUuidSubject;

    public DataSource() {
        mUuidSubject = BehaviorSubject.create(mUuid);
    }

    public void setUuid(String uuid) {
        mUuid = uuid;
        mUuidSubject.onNext(mUuid);
    }

    public String getUuid() {
        return mUuid;
    }

    public void clear() {
        mUuidSubject.onNext(mUuid);
    }

    public void onError() {
        mUuidSubject.onError(new RuntimeException("Fake Error"));
    }

    public rx.Observable<String> getUuidObservable() {
        return mUuidSubject.asObservable();
    }
}
