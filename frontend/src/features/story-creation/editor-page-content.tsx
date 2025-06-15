import PageHeader from "@/components/shared/page-header";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import StoryTab from "./tabs/story/story-tab";
import CharactersTab from "./tabs/characters/characters-tab";
import ConfigsTab from "./tabs/setup/configs-tab";
import StagesTab from "./tabs/stages/stages-tab";
import CrimeTab from "./tabs/crime/crime-tab";
import { useEditorContext } from "./context/editor-context";
import { Mystery } from "@/types/creation";
import { useAuthenticatedApi } from "@/hooks";
import { useLoadCreationFromRoute } from "./hooks/useLoadCreationFromRoute";
import LoadingHeader from "@/components/shared/loading-header";
import ErrorPage from "@/pages/error-page";
import { toast } from "sonner";

type TabId = "story" | "characters" | "setup" | "stages" | "crime";
const navItems = [
  { id: "story", label: "Story Details" },
  { id: "characters", label: "Characters" },
  { id: "setup", label: "Player Setup" },
  { id: "stages", label: "Stages & Prompts" },
  { id: "crime", label: "The Crime" },
];

const EditorPageContent = () => {
  const { loading: fetchLoading, error: fetchError } =
    useLoadCreationFromRoute();
  const { story, characters, stages, playerConfigs, crime } =
    useEditorContext();
  const {
    loading: saveLoading,
    error: saveError,
    callApi,
  } = useAuthenticatedApi<Mystery, Mystery>();

  if (fetchLoading) {
    return <LoadingHeader title="Loading Story Data" />;
  } else if (fetchError) {
    return <ErrorPage message="Could not load Story Data" />;
  }

  const handleSave = async () => {
    const creation: Mystery = {
      story,
      characters,
      stages,
      setups: playerConfigs,
      crime,
    };
    toast.message("Saving Story");
    await callApi("/api/editor", "POST", creation);
    if (saveError) {
      toast.error("Could not save Story. Try again later");
    } else {
      toast.success("Saved successfully");
    }
    // Optionally, show a success message or handle errors
  };

  return (
    <div className="min-h-screen p-4 sm:p-6 lg:p-8">
      <div className="max-w-7xl mx-auto">
        <div className="flex justify-between items-center mb-6">
          <PageHeader title="Editor" />
          <div className="flex flex-row gap-2">
            <Button onClick={handleSave} disabled={saveLoading}>
              {saveLoading ? "Saving..." : "Save Story"}
            </Button>
          </div>
        </div>

        <Tabs defaultValue="story" className="w-full space-y-4">
          <TabsList
            className="w-full overflow-x-auto flex-nowrap flex gap-2 scrollbar-thin scrollbar-track-transparent"
            style={{ justifyContent: "flex-start" }}
          >
            {navItems.map((item) => (
              <TabsTrigger key={item.id} value={item.id as TabId}>
                {item.label}
              </TabsTrigger>
            ))}
          </TabsList>

          <TabsContent value="story">
            <StoryTab />
          </TabsContent>
          <TabsContent value="characters">
            <CharactersTab />
          </TabsContent>
          <TabsContent value="setup">
            <ConfigsTab />
          </TabsContent>
          <TabsContent value="stages">
            <StagesTab />
          </TabsContent>
          <TabsContent value="crime">
            <CrimeTab />
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
};

export default EditorPageContent;
