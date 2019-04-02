package com.learn.rpc.config;

import java.io.Serializable;
import java.util.Map;

public class RegistryConfig implements Serializable {

    public static final String NO_AVAILABLE = "N/A";
    private static final long serialVersionUID = 5508512956753757169L;

    /**
     * Register center address
     */
    private String address;

    /**
     * Username to login register center
     */
    private String username;

    /**
     * Password to login register center
     */
    private String password;

    /**
     * Default port for register center
     */
    private Integer port;

    /**
     * Protocol for register center
     */
    private String protocol;

    /**
     * Network transmission type
     */
    private String transporter;

    private String server;

    private String client;

    private String cluster;

    /**
     * The group the services registry in
     */
    private String group;

    private String version;

    /**
     * Request timeout in milliseconds for register center
     */
    private Integer timeout;

    /**
     * Session timeout in milliseconds for register center
     */
    private Integer session;

    /**
     * File for saving register center dynamic list
     */
    private String file;

    /**
     * Wait time before stop
     */
    private Integer wait;

    /**
     * Whether to check if register center is available when boot up
     */
    private Boolean check;

    /**
     * Whether to allow dynamic service to register on the register center
     */
    private Boolean dynamic;

    /**
     * Whether to export service on the register center
     */
    private Boolean register;

    /**
     * Whether allow to subscribe service on the register center
     */
    private Boolean subscribe;

    /**
     * The customized parameters
     */
    private Map<String, String> parameters;

    /**
     * Whether it's default
     */
    private Boolean isDefault;

    /**
     * Simple the registry. both useful for provider and consumer
     *
     * @since 2.7.0
     */
    private Boolean simplified;
    /**
     * After simplify the registry, should add some paramter individually. just for provider.
     * <p>
     * such as: extra-keys = A,b,c,d
     *
     * @since 2.7.0
     */
    private String extraKeys;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getTransporter() {
        return transporter;
    }

    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getSession() {
        return session;
    }

    public void setSession(Integer session) {
        this.session = session;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Integer getWait() {
        return wait;
    }

    public void setWait(Integer wait) {
        this.wait = wait;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Boolean getDynamic() {
        return dynamic;
    }

    public void setDynamic(Boolean dynamic) {
        this.dynamic = dynamic;
    }

    public Boolean getRegister() {
        return register;
    }

    public void setRegister(Boolean register) {
        this.register = register;
    }

    public Boolean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getSimplified() {
        return simplified;
    }

    public void setSimplified(Boolean simplified) {
        this.simplified = simplified;
    }

    public String getExtraKeys() {
        return extraKeys;
    }

    public void setExtraKeys(String extraKeys) {
        this.extraKeys = extraKeys;
    }

    @Override
    public String toString() {
        return "RegistryConfig{" +
                "address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", port=" + port +
                ", protocol='" + protocol + '\'' +
                ", transporter='" + transporter + '\'' +
                ", server='" + server + '\'' +
                ", client='" + client + '\'' +
                ", cluster='" + cluster + '\'' +
                ", group='" + group + '\'' +
                ", version='" + version + '\'' +
                ", timeout=" + timeout +
                ", session=" + session +
                ", file='" + file + '\'' +
                ", wait=" + wait +
                ", check=" + check +
                ", dynamic=" + dynamic +
                ", register=" + register +
                ", subscribe=" + subscribe +
                ", parameters=" + parameters +
                ", isDefault=" + isDefault +
                ", simplified=" + simplified +
                ", extraKeys='" + extraKeys + '\'' +
                '}';
    }
}
