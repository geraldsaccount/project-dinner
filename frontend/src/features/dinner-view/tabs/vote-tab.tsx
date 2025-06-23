import CharacterAvatar from "@/components/shared/character-avatar";
import SectionHeader from "@/components/shared/section-header";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Checkbox } from "@/components/ui/checkbox";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { useAuthenticatedApi } from "@/hooks";
import {
  ConclusionDto,
  DinnerParticipantDto,
  FinalVoteDto,
  GuestDinnerViewDto,
} from "@/types";
import { useState } from "react";
import { toast } from "sonner";

type Props = {
  userId?: string;
  dinnerId: string;
  participants: DinnerParticipantDto[];
  conclusion: ConclusionDto;
};

const VoteTab = ({ participants, conclusion, userId, dinnerId }: Props) => {
  const initialVote = conclusion.votes.find((v) => v.guestId === userId);
  const [suspectIds, setSuspectIds] = useState<string[]>(
    initialVote?.suspectIds ?? []
  );
  const [motive, setMotive] = useState<string>(initialVote?.motive ?? "");
  const { loading, error, callApi } = useAuthenticatedApi<
    GuestDinnerViewDto,
    FinalVoteDto
  >();

  if (!userId) {
    return <></>;
  }

  const toggleSuspect = (suspectId: string) => {
    setSuspectIds((prev) =>
      prev.includes(suspectId)
        ? prev.filter((id) => id !== suspectId)
        : [...prev, suspectId]
    );
  };

  const handleSubmit = async () => {
    if (!userId || !dinnerId) return;
    const vote: FinalVoteDto = {
      guestId: userId,
      suspectIds,
      motive,
    };
    await callApi(`/api/dinners/${dinnerId}`, "POST", vote);
    if (!error) {
      toast.success("Your vote was submitted successfully!");
    }
  };
  return (
    <div className="space-y-4">
      <SectionHeader
        title={conclusion.voteOpen ? "Make your accusation" : ""}
      />
      <p>
        The time has come to reveal your suspicions. Who do you think committed
        the crime, and how did they do it?
      </p>
      <Card>
        <CardHeader>
          <CardTitle>Select Criminal(s)</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {participants.map((p: DinnerParticipantDto) => (
              <Label
                key={p.character.uuid}
                className={`flex items-center space-x-3 p-3 border-2 border-primary cursor-pointer
                    ${suspectIds.includes(p.character.uuid) ? "bg-primary text-primary-foreground" : "text-primary"}`}
              >
                <Checkbox
                  checked={suspectIds.includes(p.character.uuid)}
                  onCheckedChange={() => toggleSuspect(p.character.uuid)}
                />
                <CharacterAvatar
                  character={p.character}
                  className={
                    suspectIds.includes(p.character.uuid)
                      ? "border-primary-foreground"
                      : ""
                  }
                />
                <div>
                  <p className="font-bold uppercase">{p.character.name}</p>
                  <p className="text-xs font-medium uppercase">
                    {p.character.role}
                  </p>
                </div>
              </Label>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Your Theory (Motive & How)</CardTitle>
        </CardHeader>
        <CardContent>
          <Textarea
            className="border-primary"
            placeholder="Describe the motive and how you believe the crime was committed..."
            value={motive}
            onChange={(e) => setMotive(e.target.value)}
          />
        </CardContent>
      </Card>
      <div className="flex justify-center">
        <Button onClick={handleSubmit} disabled={loading}>
          {loading ? "Submitting..." : "Submit vote"}
        </Button>
      </div>
    </div>
  );
};

export default VoteTab;
