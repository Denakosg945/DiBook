<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.dbook.service.Topic, com.dbook.service.Message" %>

<%
    String messageUploadedParam = request.getParameter("messageUploaded");
    String messageCountParam = request.getParameter("messageCount");
%>

<%
    // Grab topicId (if any) so we can pre-select & load messages
    String topicIdParam = request.getParameter("topicId");
    int topicId = (topicIdParam != null) 
                  ? Integer.parseInt(topicIdParam) 
                  : 0;

    List<Topic> topics = Topic.getAllTopics();
    List<Message> messages = (topicId > 0)
                             ? Message.getAllMessages(topicId)
                             : null;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Topic</title>

    <!-- Your login-check script -->
    <script>
        window.onload = async function(){
            const response = await fetch("/DBook/CheckLogin",{
                method: 'GET',
                credentials: 'include' // sends cookies
            });
            const data = await response.json();
            if(!data.logged){
                window.location.href = "/DBook";
            }
        }
    </script>

    <!-- Your module import for topicPageFunctionality -->
    <script type="module">
        import { topicPageFunctionality } from './TopicPageScript.js';
        document.addEventListener("DOMContentLoaded", () => {
            topicPageFunctionality();
        });
    </script>

    <link rel="stylesheet" href="style.css">
</head>
<body class="topicMessageBody">

    <!-- —— TOPIC SELECTOR (pulled out & styled like before) —— -->
    <div id="topicSelector" class="topic-selector">
        <label for="topic">Select a Topic:</label><br>
        <select id="topic"
                class="topic-dropdown"
                onchange="if(this.value) window.location.href=window.location.pathname + '?topicId=' + this.value">
            <option value="" disabled <%= topicId==0 ? "selected" : "" %>>
                -- choose a topic --
            </option>
            <% if (topics != null) {
                   for (Topic t : topics) { %>
                <option value="<%= t.getId() %>"
                        data-description="<%= t.getDescription().replace("\"","&quot;") %>"
                        <%= (t.getId()==topicId) ? "selected" : "" %>>
                    <%= t.getName() %>
                </option>
            <%   }
               } else { %>
                <option disabled>No topics available</option>
            <% } %>
        </select>
    </div>

    

    <div id="messageDiv" class="message-container">
        <!-- Your original form (minus the topic-select) -->
        <form id="messageForm" class="message-form">
            <p id="topicDescription" class="topic-desc"></p>

            <label for="message">Your Message:</label><br>
            <textarea name="message"
                      id="message"
                      rows="5"
                      cols="50"
                      required
                      class="message-textarea"></textarea><br><br>

            <input type="submit"
                   value="Send Message"
                   class="send-button">
        </form>

        <button class="goBack" id="backBtn"><b>Go Back</b></button>
        
        <% if ("true".equals(messageUploadedParam) && messageCountParam != null) { %>
        <p style="text-align:center; font-weight:bold;">
            Total messages in this topic: <%= messageCountParam %>
        </p>
    	<% } %>
        
        <hr id="formDivider">
    </div>
    
    

    <div id="messagesSection" class="messages-section">
        <h3><b>Messages:</b></h3>
        <%
            if (messages != null) {
                for (Message m : messages) {
        %>
            <p class="message-entry">
                <b><%= m.getUsername() %></b>
                (<%= m.getDate() %>):<br>
                <%= m.getMessage() %>
            </p>
        <%
                }
            } else if (topicId > 0) {
        %>
            <p class="no-messages">No messages yet for this topic.</p>
        <% } %>
    </div>

</body>
</html>