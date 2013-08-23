General project description, project goals and use cases

The project is RSS Feed Reader and is part of small final project for the course “Android For Java Programmers.” This feed reader makes use of the rss feeds available in standard xml structure and displays them in user-friendly/appropriate format for the users. The various features for user interactions have also been implemented. Predominantly, I have based this application mainly for the feeds of BBC. The project has been developed using Android 2.3.3 platform, API level 10.
The basic goals for the project can be summarized as:
·	Parsing of the feeds available in xml and proper handling.
·	Making the relevant contents available to users.
·	Provision of user interactions to provide better user experience for the users.
·	Application of the knowledge about the android platform/programming gained in class room to more of a practical use.

Features

·	One default feed url for “BBC Sports” which is always available.
·	Use of database and content providers.
·	Use of TabActivity and ActivityGroup for user experience of Tabs. 
·	Marking the story as favorites, which saves the story in database.
·	Home screen with the tabs “Feed URLs” and “All Favorite Feeds”.
·	The tab “Feed URLs” shows all the feed urls that the user has added. On clicking an item in this list gives the stories relevant to the clicked url. Provision of adding/updating/deleting of feed urls as desired by the users which will show up inside this tab. Hence giving the users the freedom to use feeds of their choice, but I have mainly tested the application for BBC feed urls.
·	The tab “All Favorite Feeds” is a place where the user can find all the favorite stories of different feed urls which he has marked as favorites.
·	Use of AsyncTask to load the feeds for better user experience.
·	Subsequent use of favorite tabs for all the “Feed URLs”.. For example, from the home screen if I click on BBC Sports, it loads all the feeds available from BBC Sports in one tab and in other tab I will have all the stories of this BBC Sports that I marked as favorites. This will also appear in “All Favorite Feeds”.
·	Implementation of Search and Refresh functionality in the tabs which perform the search and refresh operations relevant to that particular tab.
·	Feed Stories with the title which can be viewed in detail. Further more from detailed view, a link is available which can launch a browser to view that story in the providers’ website for more information.
·	Provision of deleting from favorites. Deletion is possible as a single story or all the stories relevant to that favorite tab in one go.

Problems faced during project implementation, how they were solved

As the project used two tables: one for feed urls and other for favorites, using a Database Adapter to accommodate this took quite a bit of time. So I used one core DbAdapter that extended SQLiteOpenHelper to create a database as well as both the tables. Then further I created two separate DbAdapters, one for each table. Each of this DbAdapter handles only the DB operations (insert/update/delete/query) related to particular table.
Furthermore two separate content providers were also created which also took a lot of time.
Using of AsyncTask also was a bit of a problem initially which I solved with some tips I found on internet.
Incorporating tabs was also a problem that I faced. It wouldn’t have been a problem for a normal use. But the requirement was such that both the tabs being displayed had a relation to each other. Also from the home screen (with tabs) I launched an activity which was also displaying two tabs related to each other. For example on clicking BBC Sports from home screen opens an activity which shows all the feeds related to sports and the other tab contains all the favorite sports stories which user has marked as favorites. The problem was compounded by the fact that whenever I visit some stories and press “Back”, the previous view should be remembered. Also the different tabs had different menu options. It was quite a lot of task actually. I took cues from ActivityGroup example that I found on internet.

Problems left unsolved, ideas about possible solutions

In a detailed view, I couldn’t make the text wrap around the picture being displayed. I tried for this quite a while but due to time constraints couldn’t do it. Also I couldn’t find much for this in internet. Some suggested me that it would take quite a lot of work, so I skipped it.
One default feed url that I kept was intentional. When the database was empty with no feed url, the menu on home screen to add url was not showing up. When I corrected that, then another problem showed up. The activity launched from this home screen also contained menu options and this menu option was also showing the menu option of home screen along with its own options. Furthermore menu options of different tabs were in a conflict with each other. The problem was in the implementation when the url table is empty. So I added a default entry in the feed url table as a work around for it.
Though in a glance the functionality I have used might give impression of “not much work done” but it is not so. It was lot of work and the design part was what I overlooked.

Possible future work, additional features

Currently I have mainly used BBC Feed urls but with some good parser implementation, it can be made to work with any news feed.
More user related functions like marking as read/unread, sharing forwarding of stories can also be implemented.
Whole lots of work can be done in design to make it more sleek and fancy.
Use of Service will be better than the AsyncTask approach I have adopted. So that rather than manual refresh, some automatic periodic update of news stories is also possible.
Making a widget of the application can also be some future work.

Instructions on how to compile/run/use the application

Android 2.3.3 platform, API level 10.
Developed using IDE Eclipse Platform Versoin 3.6, Helios release.
Can be imported/compiled/run as any normal android project developed in Eclipse.

References to external resources (documents, books, projects, source codes) used in the project

Some part or whole of the source codes given in the link below have been used in this project.
http://androidgenuine.com/?tag=tabhost-nested-activity-android
http://www.vogella.de/articles/AndroidSQLite/article.html
http://www.ibm.com/developerworks/xml/tutorials/x-androidrss/index.html
http://developer.android.com/resources/tutorials/views/hello-tabwidget.html
Also from interaction in forums in
http://stackoverflow.com/

