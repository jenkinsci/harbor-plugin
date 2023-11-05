package io.jenkins.plugins.harbor.action;

import io.jenkins.plugins.harbor.action.model.WebhookEventPayload;

public class HarborWebhookEvent {
    private WebhookEventPayload webhookEventPayload;
    private String digest;
    private String name;
    private String namespace;
    private String tag;
    private String resourceUrl;

    public HarborWebhookEvent(
            WebhookEventPayload webhookEventPayload,
            String digest,
            String name,
            String namespace,
            String tag,
            String resourceUrl) {
        this.webhookEventPayload = webhookEventPayload;
        this.digest = digest;
        this.name = name;
        this.namespace = namespace;
        this.tag = tag;
        this.resourceUrl = resourceUrl;
    }

    public HarborWebhookEvent(WebhookEventPayload webhookEventPayload) {
        this.webhookEventPayload = webhookEventPayload;
        this.digest = webhookEventPayload.getEventData().getResources()[0].getDigest();
        this.name = webhookEventPayload.getEventData().getRepository().getName();
        this.namespace = webhookEventPayload.getEventData().getRepository().getNamespace();
        this.tag = webhookEventPayload.getEventData().getResources()[0].getTag();
        this.resourceUrl = webhookEventPayload.getEventData().getResources()[0].getResourceURL();
    }

    public WebhookEventPayload getWebhookEventPayload() {
        return webhookEventPayload;
    }

    public void setWebhookEventPayload(WebhookEventPayload webhookEventPayload) {
        this.webhookEventPayload = webhookEventPayload;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getImageName() {
        return String.format("%s/%s:%s@%s", namespace, name, tag, digest);
    }
}
