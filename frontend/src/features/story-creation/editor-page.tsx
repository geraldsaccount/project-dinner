import EditorProvider from "./context/editor-provider";
import EditorPageContent from "./editor-page-content";

export default function EditorPage() {
  return (
    <EditorProvider>
      <EditorPageContent />
    </EditorProvider>
  );
}
