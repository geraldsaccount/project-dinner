import { Button } from "@/components/ui/button";
import { useAuthenticatedApi } from "@/hooks";

type Props = {
  inviteCode: string;
  accepting: boolean;
  setAccepting: (accepting: boolean) => void;
};

const CharacterChoiceSection = ({
  inviteCode,
  accepting,
  setAccepting,
}: Props) => {
  const { error, callApi: putInvitation } = useAuthenticatedApi<string>();
  const handleCharacterChoice = async () => {
    setAccepting(true);
    await putInvitation(`/api/invite/${inviteCode}`, "PUT");
    setAccepting(false);
    if (!error) {
      window.location.reload();
    } else {
      window.alert("Failed to accept invite. Please try again.");
    }
  };
  return (
    <div className="gap-1">
      <Button onClick={handleCharacterChoice} disabled={accepting}>
        Choose Character
      </Button>
    </div>
  );
};

export default CharacterChoiceSection;
