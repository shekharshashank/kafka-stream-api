
package types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "text",
    "user_id",
    "followers_count",
    "screen_name"
})
public class Retweet {

    @JsonProperty("text")
    private String text;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("followers_count")
    private Integer followersCount;
    @JsonProperty("screen_name")
    private String screenName;

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    public Retweet withText(String text) {
        this.text = text;
        return this;
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Retweet withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    @JsonProperty("followers_count")
    public Integer getFollowersCount() {
        return followersCount;
    }

    @JsonProperty("followers_count")
    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Retweet withFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
        return this;
    }

    @JsonProperty("screen_name")
    public String getScreenName() {
        return screenName;
    }

    @JsonProperty("screen_name")
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Retweet withScreenName(String screenName) {
        this.screenName = screenName;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("text", text).append("userId", userId).append("followersCount", followersCount).append("screenName", screenName).toString();
    }

}
