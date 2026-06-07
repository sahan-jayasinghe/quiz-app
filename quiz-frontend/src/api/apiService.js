// ============================================================
// FILE: src/api/apiService.js
// PURPOSE: All communication with the Spring Boot backend lives
//          here. Keeping API logic separate from UI logic is a
//          best practice called "separation of concerns".
//
// WHY "/api" PREFIX?
// In vite.config.js we set up a proxy rule:
//   Any request starting with "/api" → forwarded to localhost:8080
//   with "/api" stripped from the path.
//
// So:  fetch("/api/auth/login")
//   →  Vite proxy rewrites to  http://localhost:8080/auth/login
//
// This avoids CORS errors during development because the request
// appears to come from the same origin as the React dev server.
// ============================================================

// All our fetch() calls start with "/api".
// Vite's dev server proxy strips "/api" and forwards the rest
// to your Spring Boot server at http://localhost:8080.
const BASE_URL = "/api";

// -------------------------------------------------------
// FUNCTION: login
// Sends the user's credentials to POST /auth/login.
// Returns the JWT token string on success.
// Throws an Error on failure so the caller can catch it.
// -------------------------------------------------------
export async function login(username, password) {
  // fetch() is the browser's built-in function for making HTTP requests.
  // It returns a Promise — a placeholder for a value that will arrive later.
  const response = await fetch(`${BASE_URL}/auth/login`, {
    // "await" pauses this async function until fetch() finishes.
    // Without await, response would be a Promise, not the HTTP response.

    method: "POST",   // We are sending data, so we use HTTP POST

    headers: {
      // "Content-Type: application/json" tells the server
      // that the request body is a JSON string.
      "Content-Type": "application/json",
    },

    // JSON.stringify() converts a JS object into a JSON string:
    // { username: "admin", password: "1234" }
    //   → '{"username":"admin","password":"1234"}'
    body: JSON.stringify({ username, password }),
    // { username, password } is shorthand for { username: username, password: password }
  });

  // response.json() reads the body and parses JSON → JS object.
  const data = await response.json();

  // Your backend wraps every response in ApiResponse<T>:
  // { success: true, data: { token: "..." }, message: "success", errors: null }
  if (!data.success) {
    throw new Error(data.message || "Login failed");
  }

  // data.data is the AuthResponse: { token: "eyJ..." }
  return data.data.token;
}

// -------------------------------------------------------
// FUNCTION: getAllQuestions
// Sends a GET request to /question/allquestions.
// The JWT token goes in the Authorization header so Spring
// Security's JwtAuthenticationFilter can authenticate the user.
// Returns an array of QuestionDto objects on success.
// -------------------------------------------------------
export async function getAllQuestions(token) {
  const response = await fetch(`${BASE_URL}/question/allquestions`, {
    method: "GET",

    headers: {
      // "Bearer <token>" is the standard JWT auth header format.
      // Your JwtAuthenticationFilter reads this to identify the user.
      Authorization: `Bearer ${token}`,
    },
  });

  const data = await response.json();

  if (!data.success) {
    throw new Error(data.message || "Failed to fetch questions");
  }

  // data.data is the List<QuestionDto> from Spring Boot.
  // Each QuestionDto has: id, questionTitle, option1–4,
  // rightAnswer, difficultyLevel, category.
  return data.data;
}
