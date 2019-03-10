# Frost

**Over View**

The Frost app fetches the video content from the given api and parse the response in a recursive pattern to find the out the video details.Once we have the video details,the integrated Exoplayer play the video. It has the left navigation drawer which contains multi level expandable list.


**Technical Components**

1. Activities,Thread Pool Executors, HttpURLConnection, JSON Parser & Adapters.
2. Broadcast Receivers for network change status.
3. Exoplayer for playing the video.
4. Singleton and Delegate design patterns are used to handle Api request and response.


**Improvements**

1. Preservation of data can be handled by placing the Android serialization(Through parcel able).
2. Callback mechanism can be improved for API calls for error handling.
3. Need to handle video player states for better user experience.


**Output**


