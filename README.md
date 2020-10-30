# Bookshelf

### Introduction

This is a simple Android app for reading books. It displays a list of sample books of the EPUB3 format and allows you to open them for further reading.

### The main aspects of what has been implemented

The architecture:
* it is shaped by the Clean Architecture paradigm. In the sense that the central layer of the app (business logic) is self-sufficient and independent from the external layers, while the external layers (UI and repo) depend on the central layer.
* the MVVM pattern is used to organize the relationship between different layers and sublayers of the app in an efficient way.
* all over the app, entities are rather cohesive and decoupled from each other, which is facilitated by the use of a Dependency Injection framework.

Fetching books:
* for simplicity, the app uses a special assets folder as the backend. That folder is packaged into the app. And that's where the app gets EPUB3 files from, implying all files in that folder are of the EPUB3 format. Here's the path to that folder (relative to the root of this repo): app/src/main/assets/books. In the folder, files are sorted by their names. Currently, the folder contains 20 EPUB3 files.
* fetching a book consists of the following steps: retrieve an EPUB3 file, unzip that file, extract the required metadata and save that metadata in the DB. Those steps produce a representation of the book the app can operate with (so that the app can display a book at any later point, for example).      
    
  And those steps are carried out within a dedicated service which does its job irrespective of whether the app is running. So, for example, if the user has closed the app during a sync with the assets folder it won't affect the sync, next time the user opens the app he will be presented with the progress/results of the same sync.
* books are fetched in batches. That way books are fetched gradually and only when they become needed (for example, when the user is scrolling down in the book list). Currently, the batch size is 5 (meaning, initially the first 5 files are retrieved from the assets folder and processed, then the next 5 files, and so on).
* if a fetch of any book from a batch fails (due to a wrong format of the EPUB3 file, for example), then the fetch of the entire batch is immediately treated as failed and is presented to the user as such. The user can then retry the fetch of that batch (in which case the fetch starts over from the very beginning, for simplicity).
* for simplicity, the app refreshes its representation of books only if none of the books have been successfully fetched from the assets folder before. So such a refresh will happen on an initial start of the app. If you want the app to perform such a refresh at any arbitrary point, do the following on your phone: just press the Clear Data button for the app (Bookshelf) in the stock Settings app and then restart the app.
* you can experiment with the assets folder placing different files in there. However, after you’ve changed the files in that folder, clear the app’s data on the phone (as described above) and run the app from the source code of this repo again. It's important to clear the app's data before running the app in this case, otherwise:
  * if the new list of files in the assets folder is not larger than the list of files already retrieved from that folder on previous app starts, the app won't retrieve any new files from the assets folder.
  * and if the new list of files is larger, the app will retrieve only those new files that have indexes larger than the largest index in the list of files already retrieved from the assets folder. And if some of the new files to be retrieved are the same as some of the already retrieved ones, the fetch of the books will eventually fail due to a conflict in the DB (where all represented books should have unique IDs).
    
Displaying books:      
* the content of a book page becomes visible only after it has been loaded/rendered to a sufficient degree. In the meantime, a progress bar is displayed. Otherwise, the user might see some jumping content while the loading/rendering is underway.
* when the app displays a book page, it ensures that the immediate left and right pages are ready for being displayed as well (meaning, their content has been preloaded/prerendered). That way the user can have a better scrolling experience.
* to ensure all the required HTML5 features have been enabled, it's not needed to run a local HTTP server for serving HTML documents on behalf of some domain. Because it's possible to configure a WebView accordingly. So that's what I ended up doing.

### What hasn’t been implemented so far but would have to be in the future

What has been implemented is the key part and it has been implemented quite carefully. So I had to sacrifice some less mandatory parts that could be implemented later:
* I haven't split the app so that it depends on a separate reader module.

  However, if I did that, the reader module would somewhat differ from what it looks like at first glance. Namely, in addition to being responsible for actually displaying a book, it would also be responsible for preparing a book for being displayed. Meaning the module would have to be able to, among other things, unzip an EPUB3 file, extract the required metadata and save that metadata in the DB. So that the module does everything that is inherent to the task of displaying a book efficiently.
    
  An app using such a module would rely on its following capabilities:
  * perform some preparatory processing of EPUB3 files (within the repo layer of the app)
  * subscribe consumers to a flow of metadata extracted from the above files (within the business logic layer of the app)
  * create a dedicated UI fragment that automatically displays a book corresponding to particular metadata from the above flow (within the UI layer of the app)
* I haven't added automated tests. However, since I've made it so that entities are rather cohesive and decoupled from each other all over the app (as mentioned above), it means that solid ground for automated testing has been created.    

P.S. When the Jetpack Compose library has become more production-ready, it'll make sense to refactor the UI layer of the app so that its implementation is based on that library.

### Additional things that would have to be considered in case of development for production use

Fetching books:
* I think, the app would have to periodically get links to EPUB3 files, including the timestamp of each file, from some server. Then, based on that, the app would decide whether it should delete any previously downloaded files and whether it should download any new files.
* obviously, it'd make sense to download files in batches, similarly to how retrieving files from the assets folder has been implemented. However, in the case of batch downloads, it'd also make sense to explore the possibility of simultaneous downloads to increase the aggregate speed.
* it would be good if retrying a batch download resulted not in starting over from the very beginning, but in re-doing only the part that has previously failed.    
* currently, all EPUB3 files are unzipped as soon as the app retrieves them. However, to save space, it'd make sense to explore the possibility of keeping only some of the files in the unzipped state: for example, only those corresponding to a fixed amount of recently read books.    
    
Displaying books:    
* currently, a single HTML document corresponds to a single book page displayed to the user. Meaning the user often gets very large pages through which he has to navigate somehow. But it'd be much better for the app to map the content of an HTML document into multiple pages if that content doesn't fit the screen originally.
* it'd make sense to optimize the progress bar displayed until the content of a book page has become visible, so that everything is smooth enough in terms of user experience. And it'd make sense to optimize the settings of the WebView used for loading/rendering that content, so that we have an optimal performance there.
