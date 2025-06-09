import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertOctagonIcon } from "lucide-react";

const AlreadyPlayedMessage = () => {
  return (
    <Alert variant="destructive">
      <AlertOctagonIcon />
      <AlertTitle className="font-semibold">Not allowed to join.</AlertTitle>
      <AlertDescription>
        You’ve already played this mystery—try another one or host your own!
      </AlertDescription>
    </Alert>
  );
};

export default AlreadyPlayedMessage;
