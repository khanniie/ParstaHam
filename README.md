# Project 3 - *ParstaHAM*

**ParstaHAM** is a photo sharing app using Parse as its backend.

Time spent: **32** hours spent in total

## User Stories

The following **required** functionality is completed:

- [X] Style the login page to look like the real Instagram login page.
- [X] User can sign up to create a new account using Parse authentication
- [X] User can log in and log out of his or her account
- [X] The current signed in user is persisted across app restarts
- [X] User can take a photo, add a caption, and post it to "Instagram"
- [X] User can view the last 20 posts submitted to "Instagram"
- [X] User can pull to refresh the last 20 posts submitted to "Instagram"
- [X] User can tap a post to view post details, including timestamp and caption.

The following **optional** features are implemented:

- [x] Style the feed to look like the real Instagram feed.
- [X] User should switch between different tabs - viewing all posts (feed view), capture (camera and photo gallery view) and profile tabs (posts made) using a Bottom Navigation View.
- [ ] User can load more posts once he or she reaches the bottom of the feed using infinite scrolling.
- [x] Show the username and creation time for each post
- [x] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse
- User Profiles:
   - [x] Allow the logged in user to add a profile photo
   - [x] Display the profile photo with each post
   - [x] Tapping on a post's username or profile photo goes to that user's profile page
- [x] User can comment on a post and see all comments for each post in the post details screen.
- [x] User can like a post and see number of likes for each post in the post details screen.
- [X] Create a custom Camera View on your phone.
- [x] Run your app on your phone and use the camera to take the photo (**this is the post with the caption "tarini"**)


The following **additional** features are implemented:

- [X] User can tap a post to view post details, including timestamp and caption. (this became not required on the last day)

Please list two areas of the assignment you'd like to **discuss further with your peers** during the next class (examples include better ways to implement something, how to extend your app in certain ways, etc):

1. INFINITE PAGINATION!!!! The code for it was so destructive for our original code that many people put it off until the end and never had time attempt it.
2. Better understanding of fragment hiding/showing/removing.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://i.imgur.com/f4fcmBH.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />
https://i.imgur.com/f4fcmBH.gif


User page, and details page.

<img src="https://i.imgur.com/ahPv0UJ.gif" title="user page"/>

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- [Android Async Http Client](http://loopj.com/android-async-http/) - networking library
- Glide


## Notes

Describe any challenges encountered while building the app.
Time! Time was very diffcult during the assignment. I also ended up running out of time and didn't have time to make my code as clean as I was hoping.

## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
