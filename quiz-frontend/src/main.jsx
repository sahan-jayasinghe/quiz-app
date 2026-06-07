// ============================================================
// FILE: src/main.jsx
// PURPOSE: The entry point of the entire React application.
//
// REACT CONCEPT — ReactDOM & the Root:
// React does NOT control the whole browser page — it controls
// exactly ONE DOM element, called the "root". Everything React
// renders lives INSIDE that one div.
//
// Flow:
//   index.html  →  <div id="root">  →  ReactDOM mounts App here
//                                       App renders LoginPage or
//                                       QuestionsTable based on state
// ============================================================

// `React` itself (the library for building components).
import React from "react";

// `ReactDOM` is the bridge between React's virtual DOM and
// the real browser DOM. We only need it here, once.
import ReactDOM from "react-dom/client";

// Our root component — the top of the component tree.
import App from "./App.jsx";

// ── React.StrictMode ────────────────────────────────────────
// REACT CONCEPT — StrictMode:
// Wrapping the app in <React.StrictMode> tells React to run
// extra checks in DEVELOPMENT only. It will:
//   • Warn about deprecated API usage
//   • Detect accidental side effects by calling some lifecycle
//     methods twice (e.g. useEffect runs twice on mount in dev)
//   • Has ZERO effect on the production build — it is free.
// It is best practice to always keep StrictMode on.

ReactDOM.createRoot(
  // document.getElementById("root") finds the <div id="root">
  // in index.html. This is where the whole React app is injected.
  document.getElementById("root")
).render(
  <React.StrictMode>
    {/* App is the root component. All other components are
        descendants of App in the component tree. */}
    <App />
  </React.StrictMode>
);
