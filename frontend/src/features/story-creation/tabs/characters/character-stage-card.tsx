import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import FormTextarea from "@/components/ui/form-text-area";
import {
  Character,
  CharacterStageInfo,
  Stage,
  StageEvent,
} from "@/types/creation";
import { useEditorContext } from "../../context/editor-context";
import CharacterEventCard from "./character-event-card";

interface Props {
  character: Character;
  stage: Stage;
}

export const CharacterStageCard = ({ character, stage: gStage }: Props) => {
  const { updateCharacter } = useEditorContext();

  const stageInfo = character.stageInfo.find((si) => si.stageId === gStage.id);
  if (!stageInfo) {
    return null;
  }

  const handleStageInfoUpdate = (
    stageId: string,
    field: keyof CharacterStageInfo,
    value: string
  ) => {
    const newStageInfo = character.stageInfo.map((si: CharacterStageInfo) =>
      si.stageId === stageId ? { ...si, [field]: value } : si
    );
    updateCharacter({ ...character, stageInfo: newStageInfo });
  };

  const addEvent = (stageId: string) => {
    const stageInfoObj = character.stageInfo.find((si) => si.stageId === stageId);
    const nextOrder = stageInfoObj ? stageInfoObj.events.length : 0;
    const newEvent: StageEvent = {
      id: `ev_${Date.now()}`,
      time: "",
      title: "",
      description: "",
      order: nextOrder,
    };
    const newStageInfo = character.stageInfo.map((si: CharacterStageInfo) =>
      si.stageId === stageId ? { ...si, events: [...si.events, newEvent] } : si
    );
    updateCharacter({ ...character, stageInfo: newStageInfo });
  };

  return (
    <Card className="bg-slate-50">
      <CardHeader>
        <CardTitle>{gStage.title}</CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        <FormTextarea
          label="Objective Prompt"
          value={stageInfo.objectivePrompt}
          onChange={(e) =>
            handleStageInfoUpdate(gStage.id, "objectivePrompt", e.target.value)
          }
        />

        <div className="space-y-2">
          <h5 className="font-medium">Events</h5>
          {stageInfo.events.map((event: StageEvent) => (
            <CharacterEventCard
              key={event.id}
              character={character}
              stageInfo={stageInfo}
              event={event}
            />
          ))}
          <Button
            size="sm"
            variant="outline"
            onClick={() => addEvent(gStage.id)}
          >
            Add Event
          </Button>
        </div>
      </CardContent>
    </Card>
  );
};
