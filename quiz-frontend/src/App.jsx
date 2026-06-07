// ============================================================
// FILE: src/App.jsx
// PURPOSE: The ROOT component of the React application.
//
// REACT CONCEPT — Component Tree:
// React apps are structured as a tree of components.
// App is the root. It decides which top-level view to show
// (LoginPage or QuestionsTable) based on authentication state.
// This pattern is called "conditional rendering at the top level".
//
// REACT CONCEPT — Lifting State Up:
// The JWT token is needed by BOTH LoginPage (to know when to
// disappear) and QuestionsTable (to make API calls). So we store
// it in their common ancestor — App — and pass it down as props.
// This is called "lifting state up".
// ============================================================

// Import our custom hook. This handles all auth logic.
import { useAuth } from "./hooks/useAuth";

// Import the two page-level components.
import LoginPage from "./components/LoginPage";
import QuestionsTable from "./components/QuestionsTable";

// Import the global stylesheet.
import "./App.css";

// -------------------------------------------------------
// App — root component
// No props (it's the top of the tree).
// -------------------------------------------------------
function App() {
  // Destructure everything we need from our custom hook.
  // useAuth() manages the token internally using useState.
  const { token, username, error, isLoading, handleLogin, handleLogout } = useAuth();

  // ── Conditional Rendering based on auth state ──────────
  // REACT CONCEPT — Conditional Rendering:
  // If `token` is null (not logged in), show LoginPage.
  // If `token` exists (logged in), show QuestionsTable.
  //
  // React re-evaluates this every time state changes.
  // When handleLogin succeeds, `token` becomes a non-null string,
  // React re-renders App, and QuestionsTable appears automatically.
  return (
    <div className="app-root">
      {token === null ? (
        // ── NOT LOGGED IN ─────────────────────────────────
        // We pass handleLogin, error, and isLoading as props
        // so LoginPage can call handleLogin and display feedback.
        <LoginPage
          onLogin={handleLogin}
          error={error}
          isLoading={isLoading}
        />
      ) : (
        // ── LOGGED IN ─────────────────────────────────────
        // We pass token so QuestionsTable can authorize API calls.
        // We pass username for the greeting in the nav bar.
        // We pass handleLogout so the Logout button works.
        <QuestionsTable
          token={token}
          username={username}
          onLogout={handleLogout}
        />
      )}
    </div>
  );
}

// Every React app must have a default export from its root component.
export default App;
