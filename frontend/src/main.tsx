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
import InvitationPage from "./features/invitation/invitation-page.tsx";
import InviteCodePage from "./features/invitation/invite-code-page.tsx";
import DinnerPage from "./features/dinner-view/dinner-page.tsx";
import EditorPage from "./features/story-creation/story-creation-page.tsx";

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
            <Route path="dinners" element={<ProtectedRoute />}>
              <Route index element={<EventsPage />} />
              <Route path="create" element={<EventCreationPage />} />
              <Route path=":dinnerId" element={<DinnerPage />} />
              Lazy Loading
            </Route>
            <Route path="invite">
              <Route index element={<InviteCodePage />} />
              <Route path=":inviteCode" element={<InvitationPage />} />
            </Route>
            <Route path="editor">
              <Route index element={<EditorPage />} />
            </Route>
          </Route>
        </Routes>
      </Router>
    </ClerkProvider>
  </StrictMode>
);
