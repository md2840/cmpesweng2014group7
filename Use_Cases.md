https://scontent-a-fra.xx.fbcdn.net/hphotos-xaf1/v/t1.0-9/1743521_10202969053122997_1850640954_n.jpg?oh=6ddb18dfda51dae178efd65eff1a13a7&oe=5535164B

# Use Case 1 #

**Actors:** Users

**Purpose:**  Adding photo while commenting

**Pre-conditions:**

  1. User must have active registered account on the system.
  1. User must log in to the system.

**Action Items:**

  1. User will add comment and fill necessary information like tags, location etc.
  1. User will click add photo button.
  1. User will be directed to add photo page.
  1. User will add photo and push ok button.
  1. User will push send button.

**Post-conditions:**

  1. System will show user comment with other comments.
  1. Users will be able to find this comment as a result of search.
  1. User will be able to receive notification about his/her comment.

**Exception-conditions:**

  1. Photo may be bigger than permitted size. Then, system will warn user.
  1. Photo may have not compatible extention. Then, system will warn user.

**Non-behavioural Requirements:**

  1. System gives helping tips to user.
  1. System adds information to database.
  1. System provides easy interface to user.

**Assumptions:**

  1. Nothing

# Use Case 2 #

**Actors:** Users

**Purpose:**  Subscribing and Unsubscribing

**Pre-conditions:**

  1. User must have active registered account on the system.
  1. User must log in to the system.
  1. User must find the comments, people or tags beforehand.

**Action Items:**

  1. User will click subscribe button to get notifications from the relevant user.
  1. User will click unsubscribe button (which will be seen instead of subscribe button if the user is already subscribed) to quit subscription.

**Post-conditions:**

  1. System will notify the user when the subscribed user enters a new comment.

**Exception-conditions:**

  1. User cannot subscribe himself/herself.

**Non-behavioural Requirements:**

  1. System gives helping tips to user.
  1. System adds information to database.
  1. System provides easy interface to user.

**Assumptions:**

  1. Nothing

# Use Case 3 #

**Actors:** Users and admins

**Purpose:** Report a comment

**Pre-conditions:**

  1. User shall have active registered account on the system.
  1. User shall log in to the system.

**Action Items:**

  1. User will search something.
  1. A list of comments will be showed.
  1. User will click the comment to read more.
  1. User will see problematic information in comment and clicks report this comment button.

**Post-conditions:**

  1. System sends information for reporting issue to owner of comment.
  1. Comment will be deleted if it is really inappropriate.
  1. Comment will stay if it is appropriate.


**Exception-conditions:**

  1. If the user who complains writes bad words in complaint text, s/he will be warned by the administration.

**Non-behavioural Requirements:**

  1. System will give helping tips to user.
  1. System will add information to database.
  1. System will provide easy interface to user.

**Assumptions:**

  1. Nothing

# Use Case 4 #

**Actors:** Users

**Purpose:** Taking a new password

**Pre-conditions:**

  1. User shall have active registered account on the system.

**Action Items:**

  1. User will click forgot your password button.
  1. User will enter e-mail address which is registered in the system.

**Post-conditions:**

  1. The system will send a new password to the e-mail address.

**Exception-conditions:**

  1. If the user enters invalid or non-registered e-mail address, the system will want the user to enter valid e-mail address.

**Non-behavioural Requirements:**

  1. System will give helping tips to user.
  1. System will change old password to new password in database.
  1. System will provide easy interface to user.

**Assumptions:**

  1. Nothing

# Use Case 5 #

**Actors:** Users

**Purpose:** Searching for comment.

**Pre-conditions:**

  1. User shall have active registered account on the system.
  1. User shall log in to the system.

**Action Items:**

  1. User will enter keyword to search.
  1. User will click search button.

**Post-conditions:**

  1. Comments which is relevant to keyword is shown to user.

**Assumptions:**

  1. Nothing

# Use Case 6 #
**Actors:** Users

**Purpose:** Signing up and making a comment

**Pre-conditions:**
  1. Nothing

**Action Items:**
  1. User will press the sign up button.
  1. User will fill the necessary information.
  1. Confirmation e-mail will be posted to user's e-mail address.
  1. User will confirm the e-mail and new account will be opened.
  1. User will log in to the system.
  1. The most popular comments near user's location will be showed on the user's homepage.
  1. User will search something.
  1. A list of comments about what is searched by the user will be showed.
  1. User will read the comments and make a comment.
  1. User will log out.

**Post-conditions:**
  1. Notification e-mail for successful sign up will be sent to user's e-mail address.
  1. The comment will be seen by other users.
  1. User will be able to receive notification about his/her comment.

**Exception-conditions:**
  1. If the user enters invalid or non-registered e-mail address, the system will want the user to enter valid e-mail address.

**Non-behavioural Requirements:**
  1. System will give helping tips to user.
  1. System will add information to database.
  1. System will provide easy interface to user.

**Assumptions:**
  1. Nothing

# Use Case 7 #
**Actors:** Users

**Purpose:** Searching a comment with location tag

**Pre-conditions:**
  1. User must have active registered account on the system.

**Action Items:**

  1. User will log in to the system.
  1. The most popular comments near user's location will be showed on the user's homepage.
  1. User will search something.
  1. A list of comments about what is searched by the user will be showed.
  1. User will read the comments.
  1. User will log out.

**Post-conditions:**
  1. Nothing.

**Exception-conditions:**
  1. If the user enters non-registered e-mail address or username, the system will want the user to enter valid e-mail address or username.

**Non-behavioural Requirements:**
  1. System will give helping tips to user.
  1. System will provide easy interface to user.

**Assumptions:**
  1. Nothing