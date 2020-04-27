package eu.jnksoftware.europass.sdk.constants;

public enum Conversion {
    TO_PDF("/v1/document/to/pdf"),
    TO_PDF_ESP("/v1/document/to/pdf-esp"),
    TO_PDF_CV("/v1/document/to/pdf-cv"),
    TO_WORD("/v1/document/to/word"),
    TO_OPEN_DOC("/v1/document/to/opendoc"),
    TO_JSON("/v1/document/to/json"),
    TO_XML("/v1/document/to/xml"),
    TO_XML_CV("/v1/document/to/xml-cv"),
    TO_XML_ESP("/v1/document/to/xml-esp"),
    UPGRADE("/v1/document/upgrade"),
    EXTRACTION("/v1/document/extraction");

    private final String url;

    Conversion(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
