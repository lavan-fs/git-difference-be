package dev.lavan.git.difference.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GitHubService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String GITHUB_API_URL = "https://api.github.com/repos/%s/%s/commits/%s";

    public Map<String, Object> fetchCommitDetails(String owner, String repository, String oid) {
        String url = String.format(GITHUB_API_URL, owner, repository, oid);
        return restTemplate.getForObject(url, Map.class);
    }

    public Map<String, Object> displayCommitDetails(String owner, String repository, String oid) {
        String url = String.format(GITHUB_API_URL, owner, repository, oid);

        // Fetch the commit details
        Map<String, Object> commitDetails = restTemplate.getForObject(url, Map.class);

        if (commitDetails == null) {
            return new HashMap<>();
        }

        // Extract commit object from the response
        Map<String, Object> commit = (Map<String, Object>) commitDetails.get("commit");

        // Extract the necessary fields
        String messageSubject = (String) commit.get("message");
        String messageBody = getMessageBody(messageSubject);
        String authorName = (String) ((Map<String, Object>) commit.get("author")).get("name");
        String authorAvatarUrl = (String) ((Map<String, Object>) commitDetails.get("author")).get("avatar_url");
        String committedDate = (String) ((Map<?, ?>) commit.get("author")).get("date");

        // Calculate the difference in days from current date
        long daysAgo = getDaysDifference(committedDate);
        String daysAgoInWords = convertNumberToWords(daysAgo);

        // Handle parent commits (if available)
        List<Map<String, Object>> parents = (List<Map<String, Object>>) commitDetails.get("parents");
        String parentCommit = (parents != null && !parents.isEmpty()) ? (String) parents.get(0).get("sha") : "No parent commit";

        // Prepare the result map with the extracted details
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("Message Subject", messageSubject);
        resultMap.put("Message Body", messageBody);
        resultMap.put("Author", authorName);
        resultMap.put("Author Avatar", authorAvatarUrl);
        resultMap.put("Commit Date", daysAgoInWords + " days ago");
        resultMap.put("Parent Commit", parentCommit);

        return resultMap;
    }

    // Helper method to get the message body (if available)
    private String getMessageBody(String messageSubject) {
        String[] parts = messageSubject.split("\n", 2);
        return parts.length > 1 ? parts[1] : "No message body available";
    }

    // Helper method to calculate the difference in days
    private long getDaysDifference(String commitDate) {
        // Define the date format as per the GitHub API response format
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime commitLocalDate = LocalDateTime.parse(commitDate, formatter);

        // Get the current time
        LocalDateTime now = LocalDateTime.now();

        // Calculate the difference in days
        Duration duration = Duration.between(commitLocalDate, now);
        return duration.toDays();
    }

    // Helper method to convert number to words
    private String convertNumberToWords(long number) {
        if (number == 0) {
            return "zero";
        }

        String[] ones = {
                "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
                "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
                "seventeen", "eighteen", "nineteen"
        };

        String[] tens = {
                "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
        };

        if (number < 20) {
            return ones[(int) number];
        } else if (number < 100) {
            return tens[(int) (number / 10)] + (number % 10 != 0 ? " " + ones[(int) (number % 10)] : "");
        } else if (number < 1000) {
            return ones[(int) (number / 100)] + " hundred" + (number % 100 != 0 ? " and " + convertNumberToWords(number % 100) : "");
        } else {
            return number + ""; // Handling larger numbers can be added if needed
        }
    }
}
