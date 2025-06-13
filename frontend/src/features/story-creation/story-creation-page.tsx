import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { useState, useEffect } from "react";
import StoryDetailsTab from "./tabs/story-details-tab";
import { CreateCharacterDto, Story } from "@/types";
import PageHeader from "@/components/shared/page-header";

type TabId = "story" | "characters" | "setup" | "stages" | "crime";

export default function EditorPage() {
  const navItems = [
    { id: "story", label: "Story Details" },
    { id: "characters", label: "Characters" },
    { id: "setup", label: "Player Setup" },
    { id: "stages", label: "Stages & Prompts" },
    { id: "crime", label: "The Crime" },
  ];

  const [story, setStory] = useState<Story>({
    title: "",
    shopDescription: "",
    bannerImage: null,
    rules: "",
    setting: "",
    briefing: "",
  });

  return (
    <div className="min-h-screen p-4 sm:p-6 lg:p-8">
      <div className="max-w-7xl mx-auto">
        <div className="flex justify-between items-center mb-6">
          <PageHeader title="Editor" />
          <Button>Save Story</Button>
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
          <StoryDetailsTab story={story} setStory={setStory} />
        </TabsContent>
      </Tabs>
      </div>
    </div>
  );
};

export default StoryCreationPage;
