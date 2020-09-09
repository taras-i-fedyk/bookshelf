### Main aspects of what has been implemented

Fetching books:
 
* for simplicity, the app uses a certain assets folder as the backend: app/src/main/assets/books. That folder is packaged into the app. And that's where the app gets EPUB3 files from, so all files in that folder should be of the EPUB3 format. Currently, that folder contains 20 EPUB3 files.
* preparing a book consists of the following steps: retrieve an EPUB3 file, unzip that file, extract the required metadata and save that metadata in the DB. Those steps produce a representation of the book the app can operate with (so that the app can display a book at any later point, for example).  

  And those steps are carried out within a dedicated service which does its job irrespective of whether the rest of the app is running. So, for example, if the user has closed the app in the middle of a sync with the assets folder it won't affect the sync, next time the user opens the app he will be presented with the progress/results of the same sync.
* books are fetched in batches, 5 books at a time. That way books are fetched gradually and only when they become needed (i.e., when the user scrolls down in the book list).
* if a fetch of any book within a batch fails (due to a wrong format of the EPUB3 file, for example), then the fetch of the entire batch is immediately treated as failed and is presented to the user as such. The user can then retry the fetch of the batch (in which case the fetch starts over from the very beginning, for simplicity).   
* for simplicity, the app refreshes its representation of books from the assets folder only if none of the books have been successfully fetched before. So such a refresh will happen on an initial start of the app. If you want the app to perform such a refresh at an arbitrary point, just press the Clear Data button for the Bookshelf app in the stock Settings app on your phone and then restart the app.
* you can experiment with the above-mentioned assets folder placing different files in there. However, after you’ve changed the files in that folder, clear the app’s data on the phone (as described above) and run the app from the source code of this repo again. You have to clear the app's data in this case because:
  * if the new list of files in that folder is not larger than the old one, the app won't retrieve any new files from the folder.
  * if the new list of files is larger, the app would retrieve only those new files that have indexes larger than the largest index from the old list (files are sorted by their names in that folder). And if some of the new files to be retrieved are the same as some of the files from the old list, there would be a conflict in the DB (where all stored books should have unique IDs).

Displaying books:

* when the app displays a book page, it ensures that the immediate left and right pages are preloaded/prerendered as well, so that the user can have a better scrolling experience.
* the content of a book page becomes visible only after it has been loaded/rendered to a sufficient degree, until then a progress bar is displayed. Otherwise, the user might see some glitches on the screen.
* running a local HTTP server for serving HTML documents is not needed in this case. Because it's possible to configure a WebView accordingly, so that it intercepts requests and sort of serves certain documents from a required domain. So that's what I ended up doing.

Architectural notes:

* the architecture is shaped by the Clean Architecture paradigm. In the sense that the central layer of the app (business logic) is self-sufficient and independent from the external layers, while the external layers (UI and repo) depend on the central layer.
* the MVVM pattern is used to organize the relationship between different layers and sublayers of the app in an efficient way. 
* all over the app, entities are rather cohesive and decoupled from each other, which is facilitated by the use of a Dependency Injection framework.

### What hasn’t been implemented within this iteration but would have to be in the future

I implemented the key part of the app quite carefully, so I had to sacrifice some additional parts. They are more optional and can be added later:   
* I haven't split the app into modules (app and reader). However, if I did that it would somewhat differ from what was implied by the description of this test task, I think. 

  Namely, the reader module would be a more complex thing. In addition to being responsible for actually displaying a book, it would also be responsible for preparing a book for being displayed. Meaning, among other things, the module would have to be able to unzip an EPUB3 file, extract the required metadata and save that metadata in the DB. So that the module does everything that is inherent to the task of displaying a book in an efficient way. 

  An app using such a module would call something like "prepare()" for a single book or multiple books, then it would subscribe to a flow of metadata extracted from the corresponding EPUB3 file/files, and then it would pass some metadata from that flow to a dedicated UI fragment which would finally display the book. 
* I haven't added automated tests. However, since I made it so that entities are rather cohesive and decoupled from each other all over the app (as mentioned above), it means that solid ground for automated testing has been created.

### Additional things that would have to be considered in case of further development for production use

Fetching books:
* I think, some server should return links to EPUB3 files, including timestamps of each file. Then, based on that, the app decides which files it should download: if the app hasn't downloaded a file represented by a link or if it has but the file is already outdated, then the app downloads the file.
* obviously, it'd make sense to download files in batches, similarly to how retrieving files from an assets folder has been implemented. However, in the case of network downloads, it might make sense to explore the possibility of simultaneous downloads to increase the aggregate speed of batch downloads.
* it would be good if retrying a batch download would result not in starting over from the very beginning, but in re-doing only the part that has previously failed.
* currently, all EPUB3 files are unzipped as soon as the app retrieves them. However, to save space, it'd make sense to explore the possibility of keeping only some of the files in the unzipped state: for example, those corresponding to a fixed amount of recently read books.

Displaying books:
* currently, a single HTML document corresponds to a single book page displayed to the user. Meaning the user gets very large pages through which he has to navigate somehow. lt'd be much better if the app split the content of an HTML document into multiple pages (with each of them fitting the screen) before displaying the document to the user. 
* it'd make sense to optimize the progress bar displayed while the content of a book page hasn't been loaded/rendered to a sufficient degree, so that everything is smooth enough in terms of user experience. And it'd make sense to optimize the settings of WebViews used for loading/rendering the content of a book page.
