import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import { ClerkProvider } from "@clerk/clerk-react";

import "./index.css";
import RootLayout from "./layouts/root-layout.tsx";
import ProtectedRoute from "./components/protected-route.tsx";
import InvitationPage from "./features/invitation/invitation-page.tsx";
import InviteCodePage from "./features/invitation/invite-code-page.tsx";
import DinnerPage from "./features/dinner-view/dinner-page.tsx";
import EditorPage from "./features/story-creation/editor-page.tsx";
import DinnerCreationPage from "./features/dinner-creation/dinner-creation-page.tsx";
import DinnersPage from "./features/dinner-gallery/dinners-page.tsx";
import HomePage from "./features/home/home-page.tsx";

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
            <Route index element={<HomePage />} />
            <Route path="dinners" element={<ProtectedRoute />}>
              <Route index element={<DinnersPage />} />
              <Route path="host" element={<DinnerCreationPage />} />
              <Route path=":dinnerId" element={<DinnerPage />} />
              Lazy Loading
            </Route>
            <Route path="invite">
              <Route index element={<InviteCodePage />} />
              <Route path=":inviteCode" element={<InvitationPage />} />
            </Route>
            <Route path="editor">
              <Route index element={<EditorPage />} />
              <Route path=":storyId" element={<EditorPage />} />
            </Route>
          </Route>
        </Routes>
      </Router>
    </ClerkProvider>
  </StrictMode>
);
