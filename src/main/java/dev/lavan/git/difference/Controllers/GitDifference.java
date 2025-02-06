package dev.lavan.git.difference.Controllers;

import dev.lavan.git.difference.Models.FileDifference;
import dev.lavan.git.difference.Models.FileDetail;
import dev.lavan.git.difference.Models.Hunk;
import dev.lavan.git.difference.Models.LineChange;
import dev.lavan.git.difference.Services.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repositories")
public class GitDifference {

    @Autowired
    private GitHubService gitHubService;

    // Endpoint to get commit difference (diff) details
    @GetMapping("/{owner}/{repository}/commits/{oid}/diff")
    public List<FileDifference> getDifference(
            @PathVariable String owner,
            @PathVariable String repository,
            @PathVariable String oid) {

        // Fetch commit details from GitHub
        Map<String, Object> commitDetails = gitHubService.fetchCommitDetails(owner, repository, oid);

        // Extract the diff files from the commit details
        List<Map<String, Object>> files = (List<Map<String, Object>>) commitDetails.get("files");

        List<FileDifference> fileDifferences = new ArrayList<>();

        for (Map<String, Object> file : files) {
            FileDifference fileDifference = new FileDifference();

            // Set the change kind based on file status (added, modified, deleted)
            String status = (String) file.get("status");
            fileDifference.setChangeKind(status.toUpperCase());

            // Set file details for both head and base
            FileDetail headFile = new FileDetail();
            headFile.setPath((String) file.get("filename"));

            FileDetail baseFile = new FileDetail();
            baseFile.setPath((String) file.get("filename"));

            // Assuming "patch" contains the diff content
            String patch = (String) file.get("patch");

            // Parse hunks and line changes
            List<Hunk> hunks = parsePatch(patch);

            // Set parsed details to fileDifference
            fileDifference.setHeadFile(headFile);
            fileDifference.setBaseFile(baseFile);
            fileDifference.setHunks(hunks);

            fileDifferences.add(fileDifference);
        }

        return applyTransformations(fileDifferences);
    }

    // Helper method to parse the patch diff and create hunks and line changes
    private List<Hunk> parsePatch(String patch) {
        List<Hunk> hunks = new ArrayList<>();
        if (patch != null && !patch.isEmpty()) {
            String[] patchLines = patch.split("\n");
            List<String> hunkContent = new ArrayList<>();
            String header = null;
            int baseStart = 0, headStart = 0;

            for (String line : patchLines) {
                if (line.startsWith("@@")) {
                    if (!hunkContent.isEmpty() && header != null) {
                        // Add previous hunk
                        Hunk hunk = new Hunk();
                        hunk.setHeader(header);
                        hunk.setLines(parseLineChanges(baseStart, headStart, hunkContent));
                        hunks.add(hunk);
                    }

                    // Extract baseStart and headStart from hunk header
                    header = line;
                    String[] parts = line.split(" ");
                    if (parts.length >= 3) {
                        baseStart = extractStartLine(parts[1]);
                        headStart = extractStartLine(parts[2]);
                    }

                    hunkContent.clear();
                } else {
                    hunkContent.add(line); // Store line content
                }
            }

            if (!hunkContent.isEmpty() && header != null) {
                Hunk hunk = new Hunk();
                hunk.setHeader(header);
                hunk.setLines(parseLineChanges(baseStart, headStart, hunkContent));
                hunks.add(hunk);
            }
        }
        return hunks;
    }

    // Fix: Extracts start line numbers from hunk header (e.g., "-10,5" → 10, 5)
    private int extractStartLine(String part) {
        String[] parts = part.split(",");
        return Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
    }

    // Helper method to parse line changes
    private List<LineChange> parseLineChanges(int baseStart, int headStart, List<String> patchLines) {
        List<LineChange> lineChanges = new ArrayList<>();
        for (String line : patchLines) {
            LineChange lineChange = new LineChange();
            lineChange.setBaseLineNumber(baseStart);
            lineChange.setHeadLineNumber(headStart);
            lineChange.setContent(line);

            // Increment line numbers as you go through the lines
            if (line.startsWith("-")) {
                baseStart++; // For deleted lines
            } else if (line.startsWith("+")) {
                headStart++; // For added lines
            } else {
                baseStart++; // For unchanged lines
                headStart++;
            }

            lineChanges.add(lineChange);
        }
        return lineChanges;
    }

    // Method to apply transformations to filter out deleted files
    private List<FileDifference> applyTransformations(List<FileDifference> fileDifferences) {
        List<FileDifference> transformed = new ArrayList<>();
        for (FileDifference fileDifference : fileDifferences) {
            if (!fileDifference.getChangeKind().equals("DELETED")) {
                transformed.add(fileDifference);
            }
        }
        return transformed;
    }

    // Endpoint to fetch commit details (separate from diff)
    @GetMapping("/{owner}/{repository}/commits/{oid}/details")
    public Map<String, Object> getCommitDetails(
            @PathVariable String owner,
            @PathVariable String repository,
            @PathVariable String oid) {

        // Fetch commit details from GitHub service
        return gitHubService.displayCommitDetails(owner, repository, oid);
    }
}
