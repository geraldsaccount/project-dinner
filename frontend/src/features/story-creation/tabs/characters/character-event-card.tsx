import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Character, CharacterStageInfo, StageEvent } from "@/types/creation";
import { Trash2 } from "lucide-react";
import { useEditorContext } from "../../context/editor-context";

type Props = {
  character: Character;
  stageInfo: CharacterStageInfo;
  event: StageEvent;
};

const CharacterEventCard = ({ character, stageInfo, event }: Props) => {
  const { updateCharacter } = useEditorContext();

  const handleEventUpdate = (
    stageId: string,
    eventId: string,
    field: keyof StageEvent,
    value: string
  ) => {
    const newStageInfo = character.stageInfo.map((si: CharacterStageInfo) => {
      if (si.stageId === stageId) {
        const newEvents = si.events.map((ev: StageEvent) =>
          ev.id === eventId ? { ...ev, [field]: value } : ev
        );
        return { ...si, events: newEvents };
      }
      return si;
    });
    updateCharacter({ ...character, stageInfo: newStageInfo });
  };

  const handleDeleteEvent = (stageId: string, eventId: string) => {
    const newStageInfo = character.stageInfo.map((si: CharacterStageInfo) => {
      if (si.stageId === stageId) {
        const newEvents = si.events.filter(
          (ev: StageEvent) => ev.id !== eventId
        );
        return { ...si, events: newEvents };
      }
      return si;
    });
    updateCharacter({ ...character, stageInfo: newStageInfo });
  };

  return (
    <div
      key={event.id}
      className="grid grid-cols-1 md:grid-cols-3 gap-2 p-3 border rounded-md"
    >
      <Input
        value={event.time}
        onChange={(e) =>
          handleEventUpdate(stageInfo.stageId, event.id, "time", e.target.value)
        }
        placeholder="Time (e.g., 9:15 PM)"
      />
      <Input
        value={event.title}
        onChange={(e) =>
          handleEventUpdate(
            stageInfo.stageId,
            event.id,
            "title",
            e.target.value
          )
        }
        placeholder="Event Title"
      />
      <div className="flex flex-row justify-end">
        <Button
          variant="destructive"
          size="icon"
          onClick={() => handleDeleteEvent(stageInfo.stageId, event.id)}
        >
          <Trash2 />
        </Button>
      </div>
      <Textarea
        value={event.description}
        onChange={(e) =>
          handleEventUpdate(
            stageInfo.stageId,
            event.id,
            "description",
            e.target.value
          )
        }
        placeholder="Event Description"
        className="md:col-span-3"
      />
    </div>
  );
};

export default CharacterEventCard;
