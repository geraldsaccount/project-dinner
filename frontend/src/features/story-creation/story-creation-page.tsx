import PageHeader from "@/components/shared/page-header";
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { TabsContent } from "@radix-ui/react-tabs";
import StoryDetailsTab from "./tabs/story-details-tab";
import { useState } from "react";
import { StoryCreationDto } from "@/types";

const StoryCreationPage = () => {
  const navItems = [
    { id: "story", label: "Story Details" },
    { id: "characters", label: "Characters" },
    { id: "setup", label: "Player Setup" },
    { id: "stages", label: "Stages & Prompts" },
    { id: "crime", label: "The Crime" },
  ];

  const [story, setStory] = useState<StoryCreationDto>({
    title: "",
    shopDescription: "",
    bannerImage: null,
    rules: "",
    setting: "",
    briefing: "",
  });

  return (
    <div className="flex flex-col gap-4">
      <PageHeader title="Mystery Editor" />
      <Tabs defaultValue="story" className="w-full">
        <TabsList className="w-full bg-muted flex flex-wrap gap-2 h-auto">
          {navItems.map((item) => (
            <TabsTrigger key={item.id} value={item.id}>
              {item.label}
            </TabsTrigger>
          ))}
        </TabsList>
        <TabsContent value="story">
          <StoryDetailsTab story={story} setStory={setStory} />
        </TabsContent>
      </Tabs>
    </div>
  );
};

export default StoryCreationPage;
