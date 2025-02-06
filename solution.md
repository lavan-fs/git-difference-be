# Approach and Architectural Decisions
<img src="https://github.com/lavan-fs/git-difference-fe/blob/main/public/images/overal.png?raw=true" alt="Overall  Approach and Architectural Decisions">

## Technology Choices

- **Java with Spring Boot**: Chosen for its robust ecosystem, ease of creating RESTful APIs, and scalability. Spring Boot also simplifies dependency management and configuration.
- **Maven**: Used as the build tool to handle dependencies and automate the packaging process.
- **GitHub API**: The backend interacts with GitHub repositories to fetch commit details and diffs.
- **React JS**: React JS is used along with vite for faster build generation ,react provides virtual DOM manupulation which makes efficient.
## Architecture Overview

The backend follows a RESTful service architecture, exposing endpoints to retrieve commit details and file differences.

- The core functionalities are divided into services and controllers, ensuring separation of concerns.
- **Controllers** handle HTTP requests and responses.
- **Services** contain the business logic for fetching and processing commit data.
- **Data models** represent commit details and diff structures for consistency in API responses.

## Known Limitations & Trade-offs

### Performance on Large Repositories

- **Current Solution**: Fetches commit details and diffs from GitHub’s API in real time. For large repositories, this may introduce latency.
- **Trade-off**: Instead of implementing local caching, the decision was made to keep the API stateless for simplicity.
- **Potential Improvement**: Implementing Redis or an in-memory cache to store recent commit details and diffs to reduce API calls.

### Handling Rate Limits from GitHub API

- **Current Solution**: GitHub’s API has rate limits that could affect frequent requests.
- **Trade-off**: No authentication is required in the current setup, but this limits the number of requests.
- **Potential Improvement**: Adding OAuth authentication to use authenticated API requests, increasing rate limits.

### Filtering and Parsing Patch Data

- **Current Solution**: The diff parsing is basic and does not support renamed or binary files.
- **Trade-off**: Focused on standard text-based diffs first, since they are the most common use case.
- **Potential Improvement**: Implement support for renamed/binary files by handling additional Git diff metadata.

## Future Improvements

1. **Caching System for Performance**
   - Implement Redis or in-memory caching to store frequently accessed commit details and diffs.
   - Reduce API calls to GitHub and improve response times.

2. **Webhook Integration for Real-time Updates**
   - Integrate GitHub Webhooks to update commit information in real-time.
   - Allow instant updates when a new commit is pushed.

3. **Advanced Diff Analysis**
   - Improve patch parsing to include renamed, moved, and binary files.
   - Enhance hunk processing to better visualize inline changes.

4. **Authentication for Higher Rate Limits**
   - Implement OAuth authentication to use GitHub API with higher rate limits.
   - Allow users to authenticate and fetch private repositories.

5. **Logging & Monitoring**
   - Integrate logging (e.g., Logback, ELK stack) for debugging and monitoring API usage.
   - Add request tracking for performance insights.


