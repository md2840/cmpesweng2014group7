# Summary #

_Urbsource_ is a project that aims to develop an experience-sharing platform for users about a specific living place, mainly cities. It is completely designed as a comment-based project which means every user has a right to make a comment about his/her experience in that city, and all others can see it. For example, when a person sees a traffic jam in somewhere in the city, he can directly make a comment about that situation and all users can be informed about the traffic conditions. Maybe they want to rearrange their routes. Another use case of _Urbsource_ is place-assesment, such as restaurants, hospitals and schools.

_Urbsource_ will be developed such that it operates on both Web and Mobile platforms.

This document explains the detailed requirements for the project _Urbsource_. Requirements are analyzed under two main headings; functional and non-functional.

# Background Information #

This project has two component to provide users; mobile application and web application. There are several systems to keep system alive.

One of these is searching system. Searching system provides users to get necessary information from system. This system works with recommendation system. Collecting, classifying users activities provide the probable future information to application. These probable future information is given to user to make collecting necessary information easy.

And these recommendation system works with tagging system. Users shall be able to tag the contents. Hence, finding contents with similar tags helps the recommendation system.

Lastly, gamification is the system that gives users to entertainment while using the application by collecting point way.

# Requirements v.6 #

## 1. Functional Requirements ##

### 1.1. User Requirements ###

#### 1.1.1. Creating User Requirements ####

##### 1.1.1.1. Sign Up Requirements #####

1.1.1.1.1. Users shall be able to open a new account using their email addresses (Sign Up). <br />
1.1.1.1.2. Users shall be able to give the personal information of full name, description to be seen in user profile. <br />

##### 1.1.1.2. Login Requirements #####

1.1.1.2.1. Users shall log in to the system by providing their email addresses and passwords. <br />


##### 1.1.1.3. Profile Settings #####

1.1.1.3.1. Users shall be able to change their passwords in settings page. <br />
1.1.1.3.2. Users shall be able to change their profile information. <br />

##### 1.1.1.4. Password Forget Requirements #####

1.1.1.4.1. Users shall be able to request new password in home page by clicking "forgot your password?" button. <br />


#### 1.1.2. Navigating Site Requirements ####

##### 1.1.2.1. Search Requirements #####

1.1.2.1.1. Users shall be able to browse/search a place, current traffic condition or anything related to the urban life.<br />

1.1.2.1.2. Users shall use tags that includes location base tags while searching.<br />
1.1.2.1.3. Users shall use text based search to search experience texts.<br />


#### 1.1.3. Adding Experience Requirements ####

##### 1.1.3.1. Basic Operation Requirements #####

1.1.3.1.1.  Users shall be able to create experiences. Location of user shall not be important while creating experience.<br />
1.1.3.1.2.  Users shall provide explanation and be able to provide tags while creating experience. <br />
1.1.3.1.3.  Users shall be able to delete or edit their experiences.<br />
1.1.3.1.4.  Users should categorize the experience when they create them. <br />
1.1.3.1.5.  Users shall pinpoint the locations on the city map when they create experiences.<br />
1.1.3.1.6.  Users shall add the locations as text when they create experiences.<br />
1.1.3.1.7.  Users shall choose the area of search as nearby, whole city, a district or within a certain diameter.<br />


##### 1.1.3.2. Extra Features of Experiences Requirements #####

1.1.3.2.1. Users shall be able to add smiley to their comments and experiences, such as happy and sad. <br />
1.1.3.2.2. Users shall be able to attach photos to their experiences.<br />
1.1.3.2.3.  Users should be able to browse photos that are attached to the experiences.<br />
1.1.3.2.4.  Users should be able to use accordions to hide long experiences.<br />
1.1.3.2.5. Experience shall have creation times.<br />
1.1.3.2.6. Experience shall have character limit.<br />

#### 1.1.4. User Profile/User Interaction Requirements ####

##### 1.1.4.1. User Profile Requirements #####

1.1.4.1.1. User profiles shall be able to include information of fullname, description.<br />
1.1.4.1.2. User profiles shall include their previous experiences.<br />
1.1.4.1.3. User profiles shall be able to have profile pictures as gravatars. <br />


##### 1.1.4.2. Subscribe Requirements #####

1.1.4.2.1. Users shall be able to subscribe to other users.<br />
1.1.4.2.2. Users shall be able to unsubscribe to other users who are subscribed before.<br />

##### 1.1.4.3. Experience Response Requirements #####

1.1.4.3.1. Users shall be able to verify experiences that other users enter.<br />

##### 1.1.4.4. View Requirements #####

1.1.4.4.1. Users shall be able to view other users' personal information.<br />

##### 1.1.4.5. Comment Requirements #####

1.1.4.5.1.  Users shall be able to make comments about a certain experience regardless of their location.<br />
1.1.4.5.2.  Users shall be able to delete their comments about a certain experience.<br />
1.1.4.5.3.  Users shall be able to view a certain experience's comments.<br />
1.1.4.5.4.  Comments shall have creation times.<br />
1.1.4.5.5.  Comments shall have character limit.<br />

### 1.2. System Requirements ###

#### 1.2.1 Home Page ####

1.2.1.1. Home page shall be formed of a search and filter bar rather than a map. <br />

1.2.1.2. Home page shall include recommendations.<br />
1.2.1.3. Home page shall include creating experience module.<br />
1.2.1.4. Home page shall include accordion for the creating experience module.<br />
1.2.1.5. Home page shall include recent and popular experiences mixed.<br />

#### 1.2.2 Content ####

1.2.2.1. The content shall solely be composed of user experiences.<br />

#### 1.2.3. Profile ####

1.2.3.1. All users shall have profiles.<br />

#### 1.2.4 Tagging ####

1.2.4.1. Semantic tagging shall be used. For example, a search about animals shall also bring results about cats, dogs and other kinds of animals.<br />

#### 1.2.5 Rate ####

1.2.5.1. Experiences shall be ordered according to their rates.<br />

1.2.5.2. Experiences shall be rated by users as verified, outdated, liked, disliked or not an experience.<br />

1.2.5.3. Experiences shall include report feature in order to prevent malicious comments.<br />

1.2.5.4. The owner of a experiences that's rated as "not an experience" shall have negative points.<br />

1.2.5.5. E-mail shall be sent to notify about verifying, reporting comments to its owner.<br />

#### 1.2.6. Subscription ####

1.2.6.1. Notification shall be send to user when subscribed user enters a new experience.<br />

#### 1.2.7 Filter ####

1.2.7.1. After a search/filter, user experiences shall appear before any further information related to the search/filter.<br />

1.2.7.2. There shall be a map showing the related search/filter results with pinpoints.<br />

1.2.7.3. Experiences with higher rates shall appear above.<br />

1.2.7.4. Result experiments of search shall be divided on two groups of most recents, most liked and most trustable. Experiences shall be ordered according to their creation time, like number, and owners score.<br />

#### 1.2.8 Experience Details Page ####

1.2.8.1.  Experience Details page shall be formed of a certain experience and its comments<br />

1.2.8.2. Experience Details page shall enable users to add and delete comments<br />
1.2.8.3. Experience Details page shall show number of comments of the certain experience. <br />


#### 1.2.9. Expiration Time of Experiences ####

1.2.9.1. Experiences shall be either transient or permanent. This categorization shall be chosen by user.<br />

1.2.9.2. The transient experiences shall not be valid after some time. This time shall be chosen by user but it is restricted according to lifetime of tags.<br />

1.2.9.3. Transient experiences shall not be seen after that lifetime is ended even by the owner.<br />

1.2.9.4. System shall have lifetimes for tags to assign the transient experiences to a maximum lifetime.<br />


#### 1.2.10. Pinpoints ####


1.2.10.1. Pinpoints shall include radius info or names like university or stadium, if they refer to an area rather than a point.<br />


### 1.2.11. Recommendation ###

  1. 2.11.1. System shall gather information about actions of every user in the system, i.e. system follows and calculates frequencies of tags of experiences which a specific user enters or likes, then recommends comments with these tags.<br />

#### 1.2.12. Gamification ####

1.2.12.1. Gamification shall be used to reward users with total points and the experiences are shown by order according to the rule of that the experiences of users who has higher point are shown in the upper position in page.<br />

1.2.12.2. Users shall have to kind of points: verified and liked/disliked. Every verification shall increase verified points, and every not an experience or outdated shall decrease. On the other hand, every like shall increases liked/disliked point and every dislike shall decrease.<br />

#### 1.2.13. Session ####


1.2.13.1 Upon closing web browser, the log out shall be executed.<br />


#### 1.2.14. Usability ####

1.2.14.1. The manual for usage shall be provided by the system.<br />


## 2. Non-Functional Requirements ##

### 2.1. Security and Reliability ###

2.1.1 The system behind the software shall protect the personal data which like e-mail address or password of users from guests, other users, and even administrators on website. These informations shall only be able to access with special request of owner and direct access to database by database administrators.<br />
2.1.2 The system shall provide helping tips<br />
2.1.3 The maintaince of the system shall be planned beforehand and demanded at least one in two months. <br />
2.1.4 All recordes shall be copied periodically which protects the data in case of database failure.<br />


### 2.2. Portability and Modifiability ###

2.2.1 Java, which is a universally available language, shall be used for Web and Android for mobile for future portability.<br />
2.2.2 The software shall be designed in such a manner that developers are able to make changes on the software which enhances the performance of the software or meets the needs of the users.<br />

### 2.3. Availability ###

2.3.1 The application shall be supported by Java in Google Chrome 29.0, Opera 11.0 and 12.0, Internet Explorer 9.0, Mozilla Firefox 19.0 and in later versions.<br />
2.3.2 Android 4.0 or any later version shall be used and supported for mobile application usage.<br />

### 2.4. Usability ###

2.4.1 The interface and the navigation through the web pages shall be simple and easily accessible. System shall try to follow standards like ISO/TR 16982:2002 and ISO 9241.<br />
2.4.2 The language that is displayed on the interface shall be deployed in an understandable English.<br />
2.4.3 Unicode support shall be added<br />

### 2.5. Efficiency and Capacity ###

2.5.1 Web page of the web application and all descendent pages shall be accessible in 5 seconds. According to our search and calculations, the access time can change to 10 seconds if more then 500 simultanious users are active.<br />
2.5.2 The system shall be able to handle about 10 thousand of user accounts, 1 million comments (1 gigabyte) and 100 gigabyte photos.<br />

# Dictionary #

**User:** A person which is the general description of admin, member and guest. <br />
**Guest:** A person who hasn't logged in yet. <br />
**Member:** A person who has an account and logged into the system.<br />
**Admin:** A person who has the control overall system.<br />
**Tag:** A word which indicates specific information about event or place, or etc, like location, kind of event etc.<br />
**Profile:** A page which is related to specific member has some information that is contributed by member.<br />
**Experience:** Knowledge about any places or any kind of events which a user tests by himself.<br />
**Comment:** A short summary of a users' opinion about a particular experience. <br />
**Transient experience:** An experience which has specific time to expire, delete from system.<br />
**Permanent experience:** An experience which has no specific time to expire, but its owner or admins can delete it.<br />
**Rate:** A user's opinion about other user's comments such as like or dislike.<br />
**Not an experience:** A rating system, which controls the content of comment to check that it has a really experience about event or spam.<br />
**ISO/TR 16982:2002:** A standard that provides information on human-centered usability methods that can be used for design and evaluation.<br />
**ISO 9241:** A multi-part standard that covers a number of aspects of people working with computers.<br />
**Valid:** It is a concept that which indicates that comments aren't shown at search results or owners profile. <br />
**Gamification:** The use of game thinking and game mechanics in non-game contexts to engage users in solving problems.<br />
**Semantic Tagging:** is a collection of filters that transforms "regular tags" into RDF triplesso that related content can be found.
<br />
# Requirements v.5 #

## 1. Functional Requirements ##

### 1.1. User Requirements ###

#### 1.1.1. Creating User Requirements ####

##### 1.1.1.1. Sign Up Requirements #####

1.1.1.1.1. Users shall be able to open a new account using their email addresses (Sign Up). <br />
1.1.1.1.2. Users shall be able to give the personal information of full name, description to be seen in user profile. <br />

##### 1.1.1.2. Login Requirements #####

1.1.1.2.1. Users shall log in to the system by providing their email addresses and passwords. <br />


##### 1.1.1.3. Profile Settings #####

1.1.1.3.1. Users shall be able to change their passwords in settings page. <br />
1.1.1.3.2. Users shall be able to change their profile information. <br />

##### 1.1.1.4. Password Forget Requirements #####

1.1.1.4.1. Users shall be able to request new password in home page by clicking "forgot your password?" button. <br />


#### 1.1.2. Navigating Site Requirements ####

##### 1.1.2.1. Search Requirements #####

1.1.2.1.1. Users shall be able to browse/search a place, current traffic condition or anything related to the urban life.<br />

1.1.2.1.2. Users shall use tags that includes location base tags while searching.



#### 1.1.3. Adding Experience Requirements ####

##### 1.1.3.1. Basic Operation Requirements #####

1.1.3.1.1.  Users shall be able to create experiences. Location of user shall not be important while creating experience.<br />
1.1.3.1.2.  Users shall provide explanation and be able to provide tags while creating experience. <br />
1.1.3.1.3.  Users shall be able to delete or edit their comments and experiences.<br />
1.1.3.1.4.  Users should categorize the experience when they make comments. <br />
1.1.3.1.5.  Users shall be able to make comments about a certain experience regardless of their location.<br />
1.1.3.1.6.  Users shall pinpoint the locations on the city map when they make comments or experiences.<br />
1.1.3.1.7.  Users shall choose the area of search as nearby, whole city, a district or within a certain diameter.<br />

##### 1.1.3.2. Extra Features of Experiences Requirements #####

1.1.3.2.1. Users shall be able to add smiley to their comments and experiences, such as happy and sad. <br />
1.1.3.2.2. Users shall be able to attach photos to their comments.<br />
1.1.3.2.3.  Users should be able to browse photos that are attached to the comments.<br />

#### 1.1.4. User Profile/User Interaction Requirements ####

##### 1.1.4.1. User Profile Requirements #####

1.1.4.1.1. User profiles shall be able to include information of fullname, description, job, interest of users.<br />
1.1.4.1.2. User profiles shall include their previous comments.


##### 1.1.4.2. Subscribe Requirements #####

1.1.4.2.1. Users shall be able to subscribe to other users.<br />
1.1.4.2.2. Users shall be able to unsubscribe to other users who are subscribed before.<br />

##### 1.1.4.3. Comment Response Requirements #####

1.1.4.3.1. Users shall be able to verify comments that other users enter.<br />

##### 1.1.4.4. View Requirements #####

1.1.4.4.1. Users shall be able to view other users' personal information.<br />


### 1.2. System Requirements ###

#### 1.2.1 Home Page ####

1.2.1.1. Home page shall be formed of a search and filter bar rather than a map. <br />

1.2.1.2. Home page shall include recommendations.

#### 1.2.2 Content ####

1.2.2.1. The content shall solely be composed of comments regarding user experiences.<br />

#### 1.2.3. Profile ####

1.2.3.1. All users shall have profiles.<br />

#### 1.2.4 Tagging ####

1.2.4.1. Semantic tagging shall be used. For example, a search about animals shall also bring results about cats, dogs and other kinds of animals.<br />

#### 1.2.5 Rate ####

1.2.5.1. Comments shall be ordered according to their rates.<br />

1.2.5.2. Comments shall be rated by users as verified, outdated, liked, disliked or not an experience.<br />

1.2.5.3. Comments shall include report feature in order to prevent malicious comments.<br />

1.2.5.4. The owner of a comment that's rated as "not an experience" shall have negative points.<br />

1.2.5.5. E-mail shall be sent to notify about verifying, reporting comments to its owner.<br />

#### 1.2.6. Subscription ####

1.2.6.1. Notification shall be send to user when subscribed user enters a new comment.<br />

#### 1.2.7 Filter ####

1.2.7.1. After a search/filter, user comments shall appear before any further information related to the search/filter.<br />

1.2.7.2. There shall be a map showing the related search/filter results with pinpoints.<br />

1.2.7.3. Comments with higher rates shall appear above.<br />

1.2.7.4. Result experiments of search shall be divided on two groups of most recents, most liked and most trustable. Experiences shall be ordered according to their creation time, like number, and owners score.<br />


#### 1.2.8. Expiration Time of Comments ####

1.2.8.1. Comments shall be either transient or permanent. This categorization shall be chosen by user.<br />

1.2.8.2. The transient comments shall not be valid after some time. This time shall be chosen by user but it is restricted according to lifetime of tags.<br />

1.2.8.3. Transient comments shall not be seen after that lifetime is ended even by the owner.<br />

1.2.8.4. System shall have lifetimes for tags to assign the transient comments to a maximum lifetime.<br />


#### 1.2.9. Pinpoints ####


1.2.9.1. Pinpoints shall include radius info or names like university or stadium, if they refer to an area rather than a point.<br />


### 1.2.10. Recommendation ###

  1. 2.10.1. System shall gather information about actions of every user in the system, i.e. system follows and calculates frequencies of tags of comments which a specific user enters or likes, then recommends comments with these tags.<br />

#### 1.2.11. Gamification ####

1.2.11.1. Gamification shall be used to reward users with total points and the comments are shown by order according to the rule of that the comments of users who has higher point are shown in the upper position in page.<br />

1.2.11.2. Users shall have to kind of points: verified and liked/disliked. Every verification shall increase verified points, and every not an experience or outdated shall decrease. On the other hand, every like shall increases liked/disliked point and every dislike shall decrease.

#### 1.2.12. Session ####


1.2.12.1 Upon closing web browser, the log out shall be executed.<br />


#### 1.2.13. Usability ####

1.2.13.1. The manual for usage shall be provided by the system.<br />


## 2. Non-Functional Requirements ##

### 2.1. Security and Reliability ###

2.1.1 The system behind the software shall protect the personal data which like e-mail address or password of users from guests, other users, and even administrators on website. These informations shall only be able to access with special request of owner and direct access to database by database administrators.<br />
2.1.2 The system shall provide helping tips<br />
2.1.3 The maintaince of the system shall be planned beforehand and demanded at least one in two months. <br />
2.1.4 All recordes shall be copied periodically which protects the data in case of database failure.<br />


### 2.2. Portability and Modifiability ###

2.2.1 Java, which is a universally available language, shall be used for Web and Android for mobile for future portability.<br />
2.2.2 The software shall be designed in such a manner that developers are able to make changes on the software which enhances the performance of the software or meets the needs of the users.<br />

### 2.3. Availability ###

2.3.1 The application shall be supported by Java in Google Chrome 29.0, Opera 11.0 and 12.0, Internet Explorer 9.0, Mozilla Firefox 19.0 and in later versions.<br />
2.3.2 Android 4.0 or any later version shall be used and supported for mobile application usage.<br />

### 2.4. Usability ###

2.4.1 The interface and the navigation through the web pages shall be simple and easily accessible. System shall try to follow standards like ISO/TR 16982:2002 and ISO 9241.<br />
2.4.2 The language that is displayed on the interface shall be deployed in an understandable English.<br />

### 2.5. Efficiency and Capacity ###

2.5.1 Web page of the web application and all descendent pages shall be accessible in 5 seconds. According to our search and calculations, the access time can change to 10 seconds if more then 500 simultanious users are active.<br />
2.5.2 The system shall be able to handle about 10 thousand of user accounts, 1 million comments (1 gigabyte) and 100 gigabyte photos.<br />

# Dictionary #

**User:** A person which is the general description of admin, member and guest. <br />
**Guest:** A person who hasn't logged in yet. <br />
**Member:** A person who has an account and logged into the system.<br />
**Admin:** A person who has the control overall system.<br />
**Tag:** A word which indicates specific information about event or place, or etc, like location, kind of event etc.<br />
**Profile:** A page which is related to specific member has some information that is contributed by member.<br />
**Experience:** Knowledge about any places or any kind of events which a user tests by himself.<br />
**Comment:** A short summary of a user's opinion about anything he or she experienced. <br />
**Transient comment:** A comment which has specific time to expire, delete from system.<br />
**Permanent comment:** A comment which has no specific time to expire, but its owner or admins can delete it.<br />
**Rate:** A user's opinion about other user's comments such as like or dislike.<br />
**Not an experience:** A rating system, which controls the content of comment to check that it has a really experience about event or spam.<br />
**ISO/TR 16982:2002:** A standard that provides information on human-centered usability methods that can be used for design and evaluation.<br />
**ISO 9241:** A multi-part standard that covers a number of aspects of people working with computers.<br />
**Valid:** It is a concept that which indicates that comments aren't shown at search results or owners profile. <br />
**Gamification:** The use of game thinking and game mechanics in non-game contexts to engage users in solving problems.<br />
**Semantic Tagging:** is a collection of filters that transforms "regular tags" into RDF triplesso that related content can be found.
<br />


# Requirements v.4 #

## 1. Functional Requirements ##

### 1.1. User Requirements ###

1.1.1. Users shall be able to open a new account using their email addresses (Sign Up). <br />
1.1.2. Users shall log in to the system by providing their email adresses and passwords. <br />
1.1.3. Users shall be able to change their passwords in settings page. <br />
1.1.4. Users shall be able to request new password in home page by clicking "forgot your password?" button. <br />
1.1.5. Users shall be able to add smileys to their comments, such as happy and sad. <br />
1.1.6. Users shall be able to browse/search a place, current traffic condition or anything related to the urban life.<br />
1.1.7. Users should be able to browse photos that are attached to the comments.<br />
1.1.8. Users shall be able to attach photos to their comments.<br />
1.1.9. All users shall have profiles that include their previous comments.<br />
1.1.10. Users shall be able to delete or edit their comments.<br />
1.1.11. Users shall pinpoint the locations on the city map when they make comments.<br />
1.1.12. Users shall be able to make comments about a certain experience regardless of their location.<br />
1.1.13. Users should categorize the experience when they make comments. <br />
1.1.14. Users shall choose the area of search as nearby, whole city, a district or within a certain diameter.<br />
1.1.15. Users shall gain points when their comments are verified.<br />
1.1.16. Users shall be able to subscribe to other users.<br />
1.1.17. Users shall be able to unsubscribe to other users who are subscribed before.<br />
1.1.18. Users shall be able to verify comments that other users enter.<br />
1.1.19. Users shall be able to add their personal information to be viewed by others.<br />
1.1.20. Users shall be able to view other users' personal information.<br />

### 1.2. System Requirements ###

1.2.1. The content shall solely be composed of comments regarding user experiences.<br />
1.2.2. Home page shall be formed of a search and filter bar rather than a map.<br />
1.2.3. There shall be a map showing the related search/filter results with pinpoints.<br />
1.2.4. Comments shall be either transient or permanent.<br />
1.2.5. The transient comments shall not be valid after some time. <br />
1.2.6. After a search/filter, user comments shall appear before any further information related to the search/filter.<br />
1.2.7. Semantic tagging shall be used. For example, a search about animals shall also bring results about cats, dogs and other kinds of animals. <br />
1.2.8. Comments shall be rated by users as outdated, good, bad or not an experience.<br />
1.2.9. Comments shall be ordered according to their rates. <br />
1.2.10. Comments with higher rates shall appear above.<br />
1.2.11. Pinpoints shall include radius info or names like university or stadium, if they refer to an area rather than a point.<br />
1.2.12. Comments shall include report feature in order to prevent malicious comments.<br />
1.2.13. System shall gather information about actions of every user in the system, i.e. system follows and calculates frequencies of tags of comments which a specific user enters or likes, then recommends comments with these tags. <br />
1.2.14. The owner of a comment that's rated as "not an experience" shall have negative points.<br />
1.2.15. Gamification shall be used to reward users with total points and the comments are shown by order according to the rule of that the comments of users who has higher point are shown in the upper position in page. <br />
1.2.16. Upon closing web browser, the log out shall be executed.<br />
1.2.17. The manual for usage shall be provided by the system.<br />
1.2.18. E-mail shall be sended to notify about verifying, reporting comments to its owner.
1.2.19. Notification shall be send to user when subscribed user enters a new comment.

## 2. Non-Functional Requirements ##

### 2.1. Security and Reliability ###

2.1.1 The system behind the software shall protect the personal data which like e-mail address or password of users from guests, other users, and even administrators on website. These informations shall only be able to access with special request of owner and direct access to database by database administrators.<br />
2.1.2 The system shall provide helping tips<br />
2.1.3 The maintaince of the system shall be planned beforehand and demanded at least one in two months. <br />
2.1.4 All recordes shall be copied periodically which protects the data in case of database failure.<br />


### 2.2. Portability and Modifiability ###

2.2.1 Java, which is a universally available language, shall be used for Web and Android for mobile for future portability.<br />
2.2.2 The software shall be designed in such a manner that developers are able to make changes on the software which enhances the performance of the software or meets the needs of the users.<br />

### 2.3. Availability ###

2.3.1 The application shall be supported by Java in Google Chrome 29.0, Opera 11.0 and 12.0, Internet Explorer 9.0, Mozilla Firefox 19.0 and in later versions.<br />
2.3.2 Android 4.0 or any later version shall be used and supported for mobile application usage.<br />

### 2.4. Usability ###

2.4.1 The interface and the navigation through the web pages shall be simple and easily accessible. System shall try to follow standards like ISO/TR 16982:2002 and ISO 9241.<br />
2.4.2 The language that is displayed on the interface shall be deployed in an understandable English.<br />

### 2.5. Efficiency and Capacity ###

2.5.1 Web page of the web application and all descendent pages shall be accessible in 5 seconds. According to our search and calculations, the access time can change to 10 seconds if more then 500 simultanious users are active.<br />
2.5.2 The system shall be able to handle about 10 thousand of user accounts, 1 million comments (1 gigabyte) and 100 gigabyte photos.<br />

# Dictionary #

**User:** A person which is the general description of admin, member and guest. <br />
**Guest:** A person who hasn't logged in yet. <br />
**Member:** A person who has an account and logged into the system.<br />
**Admin:** A person who has the control overall system.<br />
**Tag:** A word which indicates specific information about event or place, or etc, like location, kind of event etc.<br />
**Profile:** A page which is related to specific member has some information that is contributed by member.<br />
**Experience:** Knowledge about any places or any kind of events which a user tests by himself.<br />
**Comment:** A short summary of a user's opinion about anything he or she experienced. <br />
**Transient comment:** A comment which has specific time to expire, delete from system.<br />
**Permanent comment:** A comment which has no specific time to expire, but its owner or admins can delete it.<br />
**Rate:** A user's opinion about other user's comments such as like or dislike.<br />
**Not an experience:** A rating system, which controls the content of comment to check that it has a really experience about event or spam.<br />
**ISO/TR 16982:2002:** A standard that provides information on human-centered usability methods that can be used for design and evaluation.<br />
**ISO 9241:** A multi-part standard that covers a number of aspects of people working with computers.<br />
**Valid:** It is a concept that which indicates that comments aren't shown at search results or owners profile. <br />
**Gamification:** The use of game thinking and game mechanics in non-game contexts to engage users in solving problems.<br />
**Semantic Tagging:** is a collection of filters that transforms "regular tags" into RDF triplesso that related content can be found.
<br />
# Requirements v.3 #

## 1. Functional Requirements ##

### 1.1. User Requirements ###

1.1.1. Users shall be able to open a new account using their email addresses (Sign Up). <br />
1.1.2. Users shall log in to the system by providing their email adresses and passwords. <br />
1.1.3. Users shall be able to change their passwords in settings page. <br />
1.1.4. Users shall be able to request new password in home page by clicking "forgot your password?" button. <br />
1.1.5. Users shall be able to add smileys to their comments, such as happy and sad. <br />
1.1.6. Users shall be able to browse/search a place, current traffic condition or anything related to the urban life.<br />
1.1.7. Users should be able to browse photos that are attached to the comments.<br />
1.1.8. Users shall be able to attach photos to their comments.<br />
1.1.9. All users shall have profiles that includes their previous comments.<br />
1.1.10. Users shall be able to delete or edit their comments.<br />
1.1.11. Users shall pinpoint the locations on the city map when they are making comments.<br />
1.1.12. Users shall be able to make comments about a certain experience regardless of their location.<br />
1.1.13. Users should categorize the experience when they are making comments. <br />
1.1.14. Users shall choose the area of search as nearby, whole city, a district or within a certain diameter.<br />
1.1.15. Users shall gain points when their comments are verified.<br />
1.1.16. Users shall be able to subscribe to other users.<br />
1.1.17. Users shall be able to unsubscribe to other users who is subscribed before.<br />
1.1.18. Users shall be able to verify comments that other users enter.<br />


### 1.2. System Requirements ###

1.2.1. The content shall solely be composed of comments regarding user experiences.<br />
1.2.2. Home page shall be formed of a search and filter bar rather than a map.<br />
1.2.3. There shall be a map showing the related search/filter results with pinpoints.<br />
1.2.4. Comments shall be either transient or permanent.<br />
1.2.5. The transient comments shall not be valid after some time. <br />
1.2.6. After a search/filter, user comments shall appear before any further information related to the search/filter.<br />
1.2.7. Semantic tagging shall be used. For example, a search about animals shall also bring results about cats, dogs and other kinds of animals. <br />
1.2.8. Comments shall be rated by users as outdated, good, bad or not an experience.<br />
1.2.9. Comments shall be ordered according to their rates. <br />
1.2.10. Comments with higher rates shall appear above.<br />
1.2.11. Pinpoints shall include radius info or names like university or stadium, if they refer to an area rather than a point.<br />
1.2.12. Comments shall include report feature in order to prevent malicious comments.<br />
1.2.13. System shall gather information about actions of every user in the system, i.e. system follows and calculates frequencies of tags of comments which a specific user enters or likes, then recommends comments with these tags. <br />
1.2.14. The owner of a comment that's rated as "not an experience" shall have negative points.<br />
1.2.15. Gamification shall be used to reward users with total points and the comments are shown by order according to the rule of that the comments of users who has higher point are shown in the upper position in page. <br />
1.2.17. Upon closing web browser, the log out shall be executed.<br />
1.2.18. The manual for usage shall be provided by the system.<br />
1.2.19. E-mail shall be sended to notify about verifying, reporting comments to its owner.
1.2.20. Notification shall be send to user when subscribed user enters a new comment.

## 2. Non-Functional Requirements ##

### 2.1. Security and Reliability ###

2.1.1 The system behind the software shall protect the personal data which like e-mail address or password of users from guests, other users, and even administrators on website. These informations shall only be able to access with special request of owner and direct access to database by database administrators.<br />
2.1.2 The system shall provide helping tips within<br />
2.1.3 The maintaince of the system shall be planned beforehand and demanded at least one in two months. <br />
2.1.4 All recordes shall be copied periodically which protects the data in case of database failure.<br />


### 2.2. Portability and Modifiability ###

2.2.1 Java, which is a universally available language, shall be used for Web and Android for mobile for future portability.<br />
2.2.2 The software shall be designed in such a manner that developers are able to make changes on the software which enhances the performance of the software or meets the needs of the users.<br />

### 2.3. Availability ###

2.3.1 The application shall be supported by Java in Google Chrome 29.0, Opera 11.0 and 12.0, Internet Explorer 9.0, Mozilla Firefox 19.0 and in later versions.<br />
2.3.2 Android 4.0 or any later version shall be used and supported for mobile application usage.<br />

### 2.4. Usability ###

2.4.1 The interface and the navigation through the web pages shall be simple and easily accessible. System shall try to follow standards like ISO/TR 16982:2002 and ISO 9241.<br />
2.4.2 The language that is displayed on the interface shall be deployed in an understandable English.<br />

### 2.5. Efficiency and Capacity ###

2.5.1 Web page of the web application and all descendent pages shall be accessible in 5 seconds. According to our search and calculations, the access time can change to 10 seconds if more then 500 simultanious users are active.<br />
2.5.2 The system shall be able to handle about 10 thousand of user accounts, 1 million comments (1 gigabyte) and 100 gigabyte photos.<br />

# Dictionary #

**User:** A person which is the general description of admin, member and guest. <br />
**Guest:** A person who hasn't logged in yet. <br />
**Member:** A person who has an account and logged into the system.<br />
**Admin:** A person who has the control overall system.<br />
**Tag:** A word which indicates specific information about event or place, or etc, like location, kind of event etc.<br />
**Profile:** A page which is related to specific member has some information that is contributed by member.<br />
**Experience:** Knowledge about any places or any kind of events which a user tests by himself.<br />
**Comment:** A short summary of a user's opinion about anything he or she experienced. <br />
**Transient comment:** A comment which has specific time to expire, delete from system.<br />
**Permanent comment:** A comment which has no specific time to expire, but its owner or admins can delete it.<br />
**Rate:** A user's opinion about other user's comments such as like or dislike.<br />
**Not an experience:** A rating system, which controls the content of comment to check that it has a really experience about event or spam.<br />
**ISO/TR 16982:2002:** A standard that provides information on human-centered usability methods that can be used for design and evaluation.<br />
**ISO 9241:** A multi-part standard that covers a number of aspects of people working with computers.<br />
**Valid:** It is a concept that which indicates that comments aren't shown at search results or owners profile.


# Requirements v.2 #


## Functional Requirements ##

  1. The project shall only be user experience-based.
  1. Users shall be able to open a new account using their email addresses (Sign Up).
  1. Users shall log in to the system by providing their email adresses and passwords.
  1. Home page shall be formed of a search and filter bar rather than a map.
  1. There shall be a map showing the related search results.
  1. Users shall be able to search a place, current traffic condition or anything related to the urban life.
  1. Users should be able to browse photos.
  1. Users shall provide data with their comments.
  1. Users should be able to attach photos to their comments.
  1. Comments shall be either transient or permanent.
  1. The transient comments shall not be valid after some time.
  1. All users shall have profiles that includes their previous comments.
  1. Users shall be able to delete or edit their comments.
  1. Users shall tag the locations (pinpoint) on the city map when they are making comments.
  1. Users shall be able to make comments about a certain experience regardless of their location.
  1. Users should categorize the experience when they are making comments.
  1. After a search, user comments shall appear before any further information related to the search.
  1. Users shall choose the area of search as nearby, whole city, a district or within a certain diameter.
  1. Users shall be able to add smileys to their comments, such as happy and sad.
  1. Semantic tagging shall be used. For example, a search about animals shall also bring results about cats, dogs and other kinds of animals.
  1. Comments shall be rated by users as outdated, good, bad or not an experience.
  1. Comments shall be ordered according to their rates.
  1. Comments with higher rates shall appear above.
  1. Pinpoints shall include radius info or names like university or stadium, if they refer to an area rather than a point.
  1. Comments shall include report feature in order to prevent malicious comments.
  1. Users shall be able to subscribe to other users.
  1. System shall gather information about actions of every user in the system, i.e. system follows and calculates frequencies of tags of comments which a specific user enters or likes, then recommends comments with these tags.
  1. Users shall be able to verify comments that other users enter.
  1. Users shall gain points when their comments are verified.
  1. The owner of a comment that's rated as "not an experience" shall have negative points.
  1. Users gain points if anyone likes his/her comment and follows him/her.
  1. Gamification shall be used to reward users with total points and the comments are shown by order according to the rule of that the comments of users who has higher point are shown in the upper position in page.
  1. The system shall provide helping tips within

## Non-Functional Requirements ##
> ### Security and Reliability ###
  1. The system behind the software shall protect the personal data which like e-mail address or password of users from guests, other users, and even administrators on website. These informations shall only be able to access with special request of owner and direct access to database by database administrators.
  1. The maintaince of the system shall be planned beforehand and demanded at least one in two months.
  1. All recordes shall be copied periodically which protects the data in case of database failure.
  1. Upon closing web browser, the log out shall be executed.

> ### Portability and Modifiability ###

  1. Java, which is a universally available language, shall be used for Web and Android for mobile for future portability.
  1. The software shall be disgned in such a manner that developers are able to make changes on the software wich enhance the performance of the software or meet the needs of the users.

> ### Availability ###

  1. The application shall be supported by Java in Google Chrome 29.0, Opera 11.0 and 12.0, Internet Explorer 9.0, Mozilla Firefox 19.0 and in later versions.
  1. Android 4.0 or any later version shall be used and supported for mobile application usage.

> ### Usability ###

  1. The interface and the navigation through the web pages shall be simple and easily accessible.
  1. The language that is displayed on the interface shall be deployed in an understandable English.
  1. The manual for usage shall be provided by the system.

> ### Efficiency and Capacity ###

  1. Web page of the web application and all it descendent pages shall be accessible in 5 seconds. According to our search and calculations, the access time can change to 10 seconds if more then 500 simultanious users are active. The access spid is strongly dependent on the server that is used.
  1. The system shall be able to handle big number of records and data such as photos.
  1. The system shall minimize the traffic between a server and a mobile device by applying very efficient algorithms.


# Requirements v.1 #


## Functional Requirements ##

  1. The project shall only be user experience-based.
  1. Users shall be able to open a new account using their email addresses (Sign Up).
  1. Users shall log in to the system by providing their email adresses and passwords.
  1. Home page shall be formed of a search and filter bar rather than a map.
  1. There shall be a map showing the related search/filter results.
  1. Users shall be able to browse/search a place, current traffic condition or anything related to the urban life.
  1. Users should be able to browse photos.
  1. Users shall provide data with their comments.
  1. Users should be able to attach photos to their comments.
  1. Comments shall be either transient or permanent.
  1. The transient comments shall not be valid after some time.
  1. All users shall have profiles that includes their previous comments.
  1. Users shall be able to delete or edit their comments.
  1. Users shall tag the locations (pinpoint) on the city map when they are making comments.
  1. Users shall be able to make comments about a certain experience regardless of their location.
  1. Users should categorize the experience when they are leaving their comments.
  1. After a search/filter, user comments shall appear before any further information related to the search/filter.
  1. Users shall choose the area of search as nearby, whole city, a district or within a certain diameter.
  1. Users shall be able to tag their experiences with a smiley, as happy, sad, etc.
  1. Semantic tagging shall be used. For example, a search about animals shall also bring results about cats, dogs, etc.
  1. Comments shall be rated by users as outdated, good, bad, not an experience etc.
  1. Comments shall be ordered according to their rates.
  1. Comments with higher rates shall appear above.
  1. Pinpoints shall include radius info or names like university or stadium, if they refer to an area rather than a point.
  1. Comments shall include report feature in order to prevent malicious comments.
  1. Users shall be able to subscribe to other users.
  1. According to user statistics, recommendations that are specific to that user should appear on home page.
  1. Comments users share shall be verified by other users.
  1. Users shall gain points when their comments are verified.
  1. The owner of a comment that's rated as "not an experience" shall have negative points.
  1. Gamification shall be used to reward users with high total points.
  1. City mood map should be created showing the moods of parts of the city.Non-

## Non-Functional Requirements ##
> ### Security and Reliability ###
  1. The sytem behaind the software shall protect the personal data that user had provided.
  1. The system shall provide helping tips within
  1. The maintaince of the system shall be planned beforehand and demanded at least one in two months.
  1. All recordes shall be copied periodically which protects the data in case of database failure.
  1. Upon closing web browser, the log out shall be executed.

> ### Portability and Modifiability ###

  1. Java, which is a universally available language, shall be used for Web and Android for mobile for future portability.
  1. The software shall be disgned in such a manner that developers are able to make changes on the software wich enhance the performance of the software or meet the needs of the users.

> ### Availability ###

  1. The application shall be supported by Java in Google Chrome 29.0, Opera 11.0 and 12.0, Internet Explorer 9.0, Mozilla Firefox 19.0 and in later versions.
  1. Android 4.0 or any later version shall be used and supported for mobile application usage.

> ### Usability ###

  1. The interface and the navigation through the web pages shall be simple and easily accessible.
  1. The language that is displayed on the interface shall be deployed in an understandable English.
  1. The manual for usage shall be provided by the system.

> ### Efficiency and Capacity ###

  1. Web page of the web application and all it descendent pages shall be accessible in 5 seconds. According to our search and calculations, the access time can change to 10 seconds if more then 500 simultanious users are active. The access spid is strongly dependent on the server that is used.
  1. The system shall be able to handle big number of records and data such as photos.
  1. The system shall minimize the traffic between a server and a mobile device by applying very efficient algorithms.