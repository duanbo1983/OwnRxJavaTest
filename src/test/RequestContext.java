package test;

public class RequestContext {

    private static RequestContextThreadLocal threadLocal =
            new RequestContextThreadLocal();

    private String tag;

    /**
     * Get the RequestContext instance for the current Thread.
     *
     * @return The RequestContext
     */
    public static RequestContext getRequestContext() {
        return (RequestContext) threadLocal.get();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private static class RequestContextThreadLocal extends ThreadLocal {
        protected Object initialValue() {
            return new RequestContext();
        }
    }
}
