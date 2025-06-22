import { useEffect } from "react";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from "@/components/ui/form";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useAuthenticatedApi } from "@/hooks";
import { NewDinnerDTO, StoryForCreationDto } from "@/types";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";

import PlayerCountSlider from "./components/player-count-slider";
import GridLayout from "@/components/layout/grid-layout";
import CharacterCard from "./components/character-profile";
import SectionHeader from "../../components/shared/section-header";
import DateTimePicker from "@/components/ui/date-time-picker";
import StoryPicker from "./components/story-picker";
import StoryPickerSkeleton from "./components/story-picker-skeleton";
import PageHeader from "@/components/shared/page-header";

const eventSchema = z.object({
  storyId: z.string().min(1, "Please select a story"),
  storyConfigurationId: z.string().min(1, "Please select player count"),
  date: z.date({ required_error: "Please select a date and time" }),
});

type EventFormValues = z.infer<typeof eventSchema>;

const DinnerCreationPage = () => {
  const {
    data: stories,
    loading: storiesLoading,
    callApi: fetchStories,
  } = useAuthenticatedApi<StoryForCreationDto[]>();

  const { loading: creationLoading, callApi: postDinner } =
    useAuthenticatedApi<NewDinnerDTO>();

  const navigate = useNavigate();
  // const stories = [sampleStorySummary];
  useEffect(() => {
    fetchStories("/api/mysteries");
  }, [fetchStories]);
  console.log(stories);

  const form = useForm<EventFormValues>({
    resolver: zodResolver(eventSchema),
    defaultValues: {
      storyId: "",
      storyConfigurationId: "",
      date: undefined,
    },
  });

  const selectedStory = stories?.find((s) => s.uuid === form.watch("storyId"));
  const sortedConfigs = selectedStory
    ? [...selectedStory.configs].sort((a, b) => a.playerCount - b.playerCount)
    : [];
  const selectedConfig = sortedConfigs.find(
    (cfg) => cfg.id === form.watch("storyConfigurationId")
  );

  const handleSubmit = async (data: EventFormValues) => {
    toast("Creating Dinner");
    try {
      const body = {
        storyId: data.storyId,
        storyConfigurationId: data.storyConfigurationId,
        eventStart: data.date,
      };
      const result = await postDinner("/api/dinners", "POST", body);
      if (result && result.dinnerId) {
        navigate(`/dinners/${result.dinnerId}`);
      }
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
    } catch (_) {
      toast.error("Failed to create dinner");
    }
  };

  return (
    <Form {...form}>
      <PageHeader title="Host New Dinner" />
      <form
        onSubmit={form.handleSubmit(handleSubmit)}
        className="flex flex-col gap-4 items-baseline"
      >
        <div className="flex flex-col p-2 gap-2 w-full">
          <SectionHeader title="Step 1: Choose Story" />
          {storiesLoading ? (
            <div className="w-full md:w-[90%] flex">
              <StoryPickerSkeleton className="self-center" />
            </div>
          ) : stories && stories.length > 0 ? (
            <FormField
              control={form.control}
              name="storyId"
              render={({ field }) => (
                <FormItem>
                  <StoryPicker
                    stories={stories}
                    value={field.value}
                    onChange={field.onChange}
                  />
                  <FormMessage />
                </FormItem>
              )}
            />
          ) : (
            <div className="text-destructive">
              Failed to load stories.
              <br />
              Try again later.
            </div>
          )}
        </div>
        <div
          className={`${
            form.watch("storyId") === "" ? "hidden" : "flex"
          } flex-col p-2 gap-2 w-full`}
        >
          <SectionHeader title="Step 2: Player Count" />
          {selectedStory && sortedConfigs.length > 0 && (
            <FormField
              control={form.control}
              name="storyConfigurationId"
              render={({ field }) => (
                <FormItem>
                  <PlayerCountSlider
                    minCount={selectedStory.minPlayerCount}
                    maxCount={selectedStory.maxPlayerCount}
                    configurations={sortedConfigs}
                    selectedChanged={(cfg) => field.onChange(cfg.id)}
                    selectedConfigId={field.value}
                  />
                  <FormMessage />
                </FormItem>
              )}
            />
          )}
          {selectedConfig && (
            <div className="text-muted-foreground self-end">
              Selected player count:{" "}
              <span className="font-bold">{selectedConfig.playerCount}</span>
            </div>
          )}
          <h4 className="text-xl font-semibold">Suspects: </h4>
          <GridLayout
            className="pt-4 w-full"
            gridCols={{ base: 1, sm: 2, md: 2, xl: 3 }}
          >
            {selectedConfig?.characterIds.map((cid: string) => {
              const character = selectedStory?.characters.find(
                (c) => c.uuid === cid
              );
              return character ? (
                <CharacterCard key={cid} character={character} />
              ) : null;
            })}
          </GridLayout>
        </div>
        <div
          className={`${
            form.watch("storyId") === "" ? "hidden" : "flex"
          } flex-col p-2 gap-2 w-full`}
        >
          <SectionHeader title="Step 3: Dinner Details" />
          <FormField
            control={form.control}
            name="date"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="font-light">Date & Time</FormLabel>
                <FormControl>
                  <DateTimePicker
                    value={field.value}
                    onChange={field.onChange}
                    className="w-full sm:w-[30%] min-w-52"
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <Button
            type="submit"
            disabled={!form.formState.isValid || creationLoading}
            className="mt-4"
          >
            Create Dinner
          </Button>
        </div>
      </form>
    </Form>
  );
};

export default DinnerCreationPage;
