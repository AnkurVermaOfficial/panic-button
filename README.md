---

```markdown
# The Last-Minute Life Saver

An AI-powered productivity tool designed to deconstruct overwhelming, complex tasks into manageable, time-boxed micro-sprints.

## 🚀 Overview
When you're facing a massive project with a tight deadline, it's easy to panic. This application uses Gemini AI to instantly analyze your task and break it down into actionable steps with estimated durations, helping you regain focus and control.

## 🛠 Project Structure
```text
panic-button/
├── backend/        # Spring Boot Java Engine
└── frontend/       # React User Interface

```

## ⚙️ How to Run

### Prerequisites

* Java 17+
* Node.js 18+

### 1. Start the Backend

1. Navigate to the `backend/` folder.
2. Ensure your `application.yml` is configured with your `GEMINI_API_KEY`.
3. Run the application:
```bash
./mvnw spring-boot:run

```



### 2. Start the Frontend

1. Navigate to the `frontend/` folder.
2. Install dependencies:
```bash
npm install

```


3. Run the development server:
```bash
npm run dev

```



## ✨ Key Features

* **Intelligent Deconstruction:** Converts complex tasks into prioritized micro-sprints.
* **Interactive Timer:** Built-in start/stop functionality for each sprint.
* **Resilient Design:** Includes automated mock fallbacks to ensure the app works even if the AI service experiences high load.
* **Modern UI:** Clean, dark-mode interface designed for high-stress environments.

## 🛠 Deployment Setup

* **Backend:** Point your hosting service (like Render) to the `/backend` subdirectory.
* **Frontend:** Point your hosting service (like Vercel) to the `/frontend` subdirectory.

```

***

Once you have this saved, you are ready to push it to your GitHub repository! Is everything looking good for your presentation?

```
