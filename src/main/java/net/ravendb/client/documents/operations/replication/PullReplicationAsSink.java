package net.ravendb.client.documents.operations.replication;

import org.apache.commons.lang3.ObjectUtils;

public class PullReplicationAsSink extends ExternalReplicationBase {

    private PullReplicationMode mode = PullReplicationMode.HUB_TO_SINK;

    private String[] allowedHubToSinkPaths;
    private String[] allowedSinkToHubPaths;

    private String certificateWithPrivateKey;
    private String certificatePassword;

    private String accessName;

    private String hubName;
    private String hubDefinitionName;

    public PullReplicationAsSink() {
    }

    public PullReplicationAsSink(String database, String connectionStringName, String hubName) {
        super(database, connectionStringName);
        this.hubName = hubName;
    }

    public PullReplicationMode getMode() {
        return mode;
    }

    public void setMode(PullReplicationMode mode) {
        this.mode = mode;
    }


    public String[] getAllowedHubToSinkPaths() {
        return allowedHubToSinkPaths;
    }

    public void setAllowedHubToSinkPaths(String[] allowedHubToSinkPaths) {
        this.allowedHubToSinkPaths = allowedHubToSinkPaths;
    }

    public String[] getAllowedSinkToHubPaths() {
        return allowedSinkToHubPaths;
    }

    public void setAllowedSinkToHubPaths(String[] allowedSinkToHubPaths) {
        this.allowedSinkToHubPaths = allowedSinkToHubPaths;
    }

    public String getCertificateWithPrivateKey() {
        return certificateWithPrivateKey;
    }

    public void setCertificateWithPrivateKey(String certificateWithPrivateKey) {
        this.certificateWithPrivateKey = certificateWithPrivateKey;
    }

    public String getCertificatePassword() {
        return certificatePassword;
    }

    public void setCertificatePassword(String certificatePassword) {
        this.certificatePassword = certificatePassword;
    }

    /**
     * @deprecated Please use getHubName instead
     */
    public String getHubDefinitionName() {
        return hubDefinitionName;
    }

    /**
     * @deprecated Please use setHubName instead
     */
    public void setHubDefinitionName(String hubDefinitionName) {
        this.hubDefinitionName = hubDefinitionName;
    }

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }

    public String getHubName() {
        return ObjectUtils.firstNonNull(hubName, hubDefinitionName);
    }

    public void setHubName(String hubName) {
        this.hubName = hubName;
    }
}