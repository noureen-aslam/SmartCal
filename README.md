**SmartCal – Smart Calorie Counter App**

SmartCal is an Android application built using Java in Android Studio that helps users monitor their daily calorie intake by recognizing food items and calculating nutritional values. It aims to make healthy eating easy, accurate, and accessible for everyone.

**Table of Contents**

1. Overview
2. Features
3. Tech Stack
4. Screens and Flow
5. Installation & Setup
6. How to Run the App
7. GitHub Integration Steps
8. Future Enhancements
9. Screenshots (Optional)
10. License

**1. Overview**
SmartCal uses AI-powered image recognition and manual logging to track what users eat. It stores meal data, calculates calories, and gives daily nutritional insights to help users stay fit and meet their health goals.

This project demonstrates skills in Android app development, database integration, and AI model usage (via APIs for food recognition).

**2. Features**

**-> **Food Recognition**** – Upload/capture an image of your meal to auto-detect food items.
**-> Calorie Calculation** – View detailed calorie and nutrient breakdown for each item.
**-> Meal Tracking** – Log breakfast, lunch, and dinner entries with timestamps.
**-> Progress Dashboard** – Visualize calorie intake trends with interactive charts.
**-> Local Database Support** – Stores data using Room Database for offline tracking.
**-> Cloud Backup (Future)** – Sync user data with Firebase for backup and cross-device use.

**3. Tech Stack**
Component	Technology Used
Language	Java
IDE	Android Studio
Database	Room Database / SQLite
Design	XML, Material UI
API Integration	Retrofit (for AI image recognition)
Version Control	Git & GitHub

**4.Screens and Flow**

**Main App Flow:**
Splash Screen → Displays app logo and transitions to login.
Login/Signup → User authentication screen (Firebase integration optional).
Home Screen → Displays total calories, meal logs, and “Add Food” button.
Camera/Food Upload → Capture or upload food image for calorie detection.
Results Screen → Shows recognized food and calorie info.
History Screen → Displays all past meal entries.

**5. Installation & Setup**

Follow these steps to run the project locally:
Clone the repository:

git clone https://github.com/noureen-aslam/SmartCal.git


**Open in Android Studio:**

Open Android Studio → File → Open → Select project folder.

Sync Gradle:

Android Studio will automatically sync.

If not, click “Sync Project with Gradle Files”.

Run the app:

Connect your Android device or use an emulator.

Press Shift + F10 or click Run ▶️.

**6. How to Run the App**

Launch Android Studio.
Open the SmartCal project folder.
Wait for Gradle sync to complete.
Build the project (Build → Make Project).
Run it on your emulator or a connected Android phone.

**7. GitHub Integration Steps**

If you’re adding this project to GitHub manually:

git init
git add .
git commit -m "Initial commit: SmartCal project setup"
git branch -M main
git remote add origin https://github.com/your-username/SmartCal-App.git
git push -u origin main


Now your Android Studio project is live on GitHub!

**8. Future Enhancements**

-> Improve AI accuracy for food image recognition.
-> Add personalized diet recommendations using ML.
-> Firebase login and cloud data sync.
-> Dark mode and better UI themes.
-> Add daily reminder notifications for logging meals.





📱 /screenshots/home_screen.png
📱 /screenshots/add_food.png
📱 /screenshots/results.png

📜 License

This project is licensed under the MIT License – you’re free to use, modify, and distribute it for educational or personal purposes.
