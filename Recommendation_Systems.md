![http://www.loria.fr/~alchiekc/trancloud.png](http://www.loria.fr/~alchiekc/trancloud.png)

Recommender systems or recommendation systems (sometimes replacing "system" with a synonym such as platform or engine) are a subclass of information filtering system that seek to predict the 'rating' or 'preference' that user would give to an item.

Recommender systems have become extremely common in recent years, and are applied in a variety of applications. The most popular ones are probably movies, music, news, books, research articles, search queries, social tags, and products in general. However, there are also recommender systems for experts, jokes, restaurants, financial services, live insurances, persons (online dating), and twitter followers.

## Overview ##

Recommender systems typically produce a list of recommendations in one of two ways - through collaborative or content-based filtering. Collaborative filtering approaches build a model from a user's past behavior (items previously purchased or selected and/or numerical ratings given to those items) as well as similar decisions made by other users; then use that model to predict items (or ratings for items) that the user may have an interest in. Content-based filtering approaches utilize a series of discrete characteristics of an item in order to recommend additional items with similar properties. These approaches are often combined (see Hybrid Recommender Systems).

The differences between collaborative and content-based filtering can be demonstrated by comparing two popular music recommender systems - Last.fm and Pandora Radio.

  * Pandora uses the properties of a song or artist (a subset of the 400 attributes provided by the Music Genome Project) in order to seed a "station" that plays music with similar properties. User feedback is used to refine the station's results, deemphasizing certain attributes when a user "dislikes" a particular song and emphasizing other attributes when a user "likes" a song. This is an example of a content-based approach.
  * Last.fm creates a "station" of recommended songs by observing what bands and individual tracks that the user has listened to on a regular basis and comparing those against the listening behavior of other users. Last.fm will play tracks that do not appear in the user's library, but are often played by other users with similar interests. As this approach leverages the behavior of users, it is an example of a collaborative filtering technique.

Each type of system has its own strengths and weaknesses. In the above example, Last.fm requires a large amount of information on a user in order to make accurate recommendations. This is an example of the cold start problem, and is common in collaborative filtering systems. While Pandora needs very little information to get started, it is far more limited in scope (for example, it can only make recommendations that are similar to the original seed).

Recommender systems are a useful alternative to search algorithms since they help users discover items they might not have found by themselves. Interestingly enough, recommender systems are often implemented using search engines indexing non-traditional data.

Montaner provides the first overview of recommender systems, from an intelligent agents perspective. Adomavicius provides a new overview of recommender systems. Herlocker provides an additional overview of evaluation techniques for recommender systems, and Beel et al. discuss the problems of offline evaluations. They also provide a literature survey on research paper recommender systems.

Recommender system is an active research area in the data mining and machine learning areas. Some conferences such as RecSys, SIGIR, KDD have it as a topic.

## Approaches ##

### Collaborative filtering ###

One approach to the design of recommender systems that has seen wide use is collaborative filtering. Collaborative filtering methods are based on collecting and analyzing a large amount of information on users’ behaviors, activities or preferences and predicting what users will like based on their similarity to other users. A key advantage of the collaborative filtering approach is that it does not rely on machine analyzable content and therefore it is capable of accurately recommending complex items such as movies without requiring an "understanding" of the item itself. Many algorithms have been used in measuring user similarity or item similarity in recommender systems. For example, the k-nearest neighbor (k-NN) approach and the Pearson Correlation.

When building a model from a user's profile, a distinction is often made between explicit and implicit forms of data collection.

Examples of explicit data collection include the following:

  * Asking a user to rate an item on a sliding scale.
  * Asking a user to rank a collection of items from favorite to least favorite.
  * Presenting two items to a user and asking him/her to choose the better one of them.
  * Asking a user to create a list of items that he/she likes.

Examples of implicit data collection include the following:

  * Observing the items that a user views in an online store.
  * Analyzing item/user viewing times.
  * Keeping a record of the items that a user purchases online.
  * Obtaining a list of items that a user has listened to or watched on his/her computer.
  * Analyzing the user's social network and discovering similar likes and dislikes

The recommender system compares the collected data to similar and dissimilar data collected from others and calculates a list of recommended items for the user. Several commercial and non-commercial examples are listed in the article on collaborative filtering systems.

One of the most famous examples of collaborative filtering is item-to-item collaborative filtering (people who buy x also buy y), an algorithm popularized by Amazon.com's recommender system. Other examples include:

> As previously detailed, Last.fm recommends music based on a comparison of the listening habits of similar users.
> Facebook, MySpace, LinkedIn, and other social networks use collaborative filtering to recommend new friends, groups, and other social connections (by examining the network of connections between a user and their friends).Twitter uses many signals and in-memory computations for recommending who to follow to its users.

Collaborative filtering approaches often suffer from three problems: cold start, scalability, and sparsity.

  * Cold Start: These systems often require a large amount of existing data on a user in order to make accurate recommendations.
  * Scalability: In many of the environments that these systems make recommendations in, there are millions of users and products. Thus, a large amount of computation power is often necessary to calculate recommendations.
  * Sparsity: The number of items sold on major e-commerce sites is extremely large. The most active users will only have rated a small subset of the overall database. Thus, even the most popular items have very few ratings.

A particular type of collaborative filtering algorithm uses matrix factorization, a low-rank matrix approximation technique.

### Content-based filtering ###

Another common approach when designing recommender systems is content-based filtering. Content-based filtering methods are based on a description of the item and a profile of the user’s preference. In a content-based recommender system, keywords are used to describe the items; beside, a user profile is built to indicate the type of item this user likes. In other words, these algorithms try to recommend items that are similar to those that a user liked in the past (or is examining in the present). In particular, various candidate items are compared with items previously rated by the user and the best-matching items are recommended. This approach has its roots in information retrieval and information filtering research.

To abstract the features of the items in the system, item presentation algorithm is applied. A widely used algorithm is the tf–idf representation(also called vector space representation).

To create user profile,the system mostly focus on two types of information: 1. A model of the user's preference. 2. A history of the user's interaction with the recommendor system.

Basically, these methods use an item profile (i.e., a set of discrete attributes and features) characterizing the item within the system. The system creates a content-based profile of users based on a weighted vector of item features. The weights denote the importance of each feature to the user and can be computed from individually rated content vectors using a variety of techniques. Simple approaches use the average values of the rated item vector while other sophisticated methods use machine learning techniques such as Bayesian Classifiers, cluster analysis, decision trees, and artificial neural networks in order to estimate the probability that the user is going to like the item.

Direct feedback from a user, usually in the form of a like or dislike button, can be used to assign higher or lower weights on the importance of certain attributes (using Rocchio Classification or other similar techniques).

A key issue with content-based filtering is whether the system is able to learn user preferences from user's actions regarding one content source and use them across other content types. When the system is limited to recommending content of the same type as the user is already using, the value from the recommendation system is significantly less than when other content types from other services can be recommended. For example, recommending news articles based on browsing of news is useful, but it's much more useful when music, videos, products, discussions etc. from different services can be recommended based on news browsing.

As previously detailed, Pandora Radio is a popular example of a content-based recommender system that plays music with similar characteristics to that of a song provided by the user as an initial seed. There are also a large number of content-based recommender systems aimed at providing movie recommendations, a few such examples include Rotten Tomatoes, Internet Movie Database, Jinni, Rovi Corporation, Jaman and See This Next.


### Hybrid Recommender Systems ###

Recent research has demonstrated that a hybrid approach, combining collaborative filtering and content-based filtering could be more effective in some cases. Hybrid approaches can be implemented in several ways: by making content-based and collaborative-based predictions separately and then combining them; by adding content-based capabilities to a collaborative-based approach (and vice versa); or by unifying the approaches into one model. Several studies empirically compare the performance of the hybrid with the pure collaborative and content-based methods and demonstrate that the hybrid methods can provide more accurate recommendations than pure approaches. These methods can also be used to overcome some of the common problems in recommender systems such as cold start and the sparsity problem.

Netflix is a good example of hybrid systems. They make recommendations by comparing the watching and searching habits of similar users (i.e. collaborative filtering) as well as by offering movies that share characteristics with films that a user has rated highly (content-based filtering).

A variety of techniques have been proposed as the basis for recommender systems: collaborative, content-based, knowledge-based, and demographic techniques. Each of these techniques has known shortcomings, such as the well known cold-start problem for collaborative and content-based systems (what to do with new users with few ratings) and the knowledge engineering bottleneck in knowledge-based approaches. A hybrid recommender system is one that combines multiple techniques together to achieve some synergy between them.

  * Collaborative: The system generates recommendations using only information about rating profiles for different users. Collaborative systems locate peer users with a rating history similar to the current user and generate recommendations using this neighborhood.
  * Content-based: The system generates recommendations from two sources: the features associated with products and the ratings that a user has given them. Content-based recommenders treat recommendation as a user-specific classification problem and learn a classifier for the user's likes and dislikes based on product features.
  * Demographic: A demographic recommender provides recommendations based on a demographic profile of the user. Recommended products can be produced for different demographic niches, by combining the ratings of users in those niches.
  * Knowledge-based: A knowledge-based recommender suggests products based on inferences about a user’s needs and preferences. This knowledge will sometimes contain explicit functional knowledge about how certain product features meet user needs.

The term hybrid recommender system is used here to describe any recommender system that combines multiple recommendation techniques together to produce its output. There is no reason why several different techniques of the same type could not be hybridized, for example, two different content-based recommenders could work together, and a number of projects have investigated this type of hybrid: NewsDude, which uses both naive Bayes and kNN classifiers in its news recommendations is just one example.

Seven hybridization techniques:

  * Weighted: The score of different recommendation components are combined numerically.
  * Switching: The system chooses among recommendation components and applies the selected one.
  * Mixed: Recommendations from different recommenders are presented together.
  * Feature Combination: Features derived from different knowledge sources are combined together and given to a single recommendation algorithm.
  * Feature Augmentation: One recommendation technique is used to compute a feature or set of features, which is then part of the input to the next technique.
  * Cascade: Recommenders are given strict priority, with the lower priority ones breaking ties in the scoring of the higher ones.
  * Meta-level: One recommendation technique is applied and produces some sort of model, which is then the input used by the next technique.

## Beyond Accuracy ##

Typically, research on recommender systems is concerned about finding the most accurate recommendation algorithms. However, there is a number of factors that are also important.

**Diversity** - Users tend to be more satisfied with recommendations when there is a higher intra-list diversity, i.e. items from e.g. different artists.

**Recommender Persistence** - In some situations it is more effective to re-show recommendations, or let users re-rate items, than showing new items. There are several reasons for this. Users may ignore items when they are shown for the first time, for instance, because they had no time to inspect the recommendations carefully.

**Privacy** - Recommender systems usually have to deal with privacy concerns because users have to reveal sensitive information. Building user profiles using collaborative filtering can be problematic from a privacy point of view. Many European countries have a strong culture of data privacy and every attempt to introduce any level of user profiling can result in a negative customer response. A number of privacy issues arose around the dataset offered by Netflix for the Netflix Prize competition. Although the data sets were anonymized in order to preserve customer privacy, in 2007, two researchers from the University of Texas were able to identify individual users by matching the data sets with film ratings on the Internet Movie Database. As a result, in December 2009, an anonymous Netflix user sued Netflix in Doe v. Netflix, alleging that Netflix had violated U.S. fair trade laws and the Video Privacy Protection Act by releasing the datasets. This led in part to the cancellation of a second Netflix Prize competition in 2010. Much research has been conducted on ongoing privacy issues in this space. Ramakrishnan et al. have conducted an extensive overview of the trade-offs between personalization and privacy and found that the combination of weak ties (an unexpected connection that provides serendipitous recommendations) and other data sources can be used to uncover identities of users in an anonymized dataset.

**User Demographics** - Beel et al. found that user demographics may influence how satisfied users are with recommendations. In their paper they show that elderly users tend to be more interested in recommendations than younger users.

**Robustness** - When users can participate in the recommender system, the issue of fraud must be addressed.

**Serendipity** - Serendipity is a measure "how surprising the recommendations are". For instance, a recommender system that recommends milk to a customer in a grocery store, might be perfectly accurate but still it is not a good recommendation because it is an obvious item for the customer to buy.

**Trust** - A recommender system is of little value for a user if the user does not trust the system. Trust can be build by a recommender system by explaining how it generates recommendations, and why it recommends an item.

**Labelling** - User satisfaction with recommendations may be influenced by the labeling of the recommendations. For instance, in the cited study click-through rate (CTR) for recommendations labeled as "Sponsored" were lower (CTR=5.93%) than CTR for identical recommendations labeled as "Organic" (CTR=8.86%). Interestingly, recommendations with no label performed best (CTR=9.87%) in that study.

## Example of The System Backround ##

![http://eprints.soton.ac.uk/268537/1/figure1.jpg](http://eprints.soton.ac.uk/268537/1/figure1.jpg)

## References ##

  * [Wikipedia](http://en.wikipedia.org/wiki/Recommender_system)
  * [Foxtrot Recommender System](http://eprints.soton.ac.uk/268537/1/www-foxtrot-poster.htm)