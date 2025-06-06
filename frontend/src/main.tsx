import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import { ClerkProvider } from "@clerk/clerk-react";

import "./index.css";
import RootLayout from "./layouts/root-layout.tsx";
import LandingPage from "./pages/landing-page.tsx";
import EventsPage from "./features/event-gallery/events-page.tsx";
import ProtectedRoute from "./components/protected-route.tsx";
import EventCreationPage from "./features/event-creation/event-creation-page.tsx";

const PUBLISHABLE_KEY = import.meta.env.VITE_CLERK_PUBLISHABLE_KEY;

if (!PUBLISHABLE_KEY) {
  throw new Error("Missing Publishable Key");
}

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ClerkProvider publishableKey={PUBLISHABLE_KEY}>
      <Router>
        <Routes>
          <Route element={<RootLayout />}>
            <Route index element={<LandingPage />} />
            <Route path="events" element={<ProtectedRoute />}>
              <Route index element={<EventsPage />} />
              <Route path="create" element={<EventCreationPage />} />
              Lazy Loading
            </Route>
          </Route>
        </Routes>
      </Router>
    </ClerkProvider>
  </StrictMode>
);
