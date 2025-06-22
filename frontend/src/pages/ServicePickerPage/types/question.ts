export type AnswerValue = string[] | number[] | number | undefined;

export interface QuestionBase {
  id: number;
  questionText: string;
  isRequired: boolean;
  type: string;
  active: boolean;
  position: number;
}

export interface ReferenceQuestion extends QuestionBase {
  type: "reference";
  referenceType: "regions" | "professions";
}

export interface InputQuestion extends QuestionBase {
  type: "input";
  validation?: { min: number; max: number };
  placeholder?: string;
}

export interface SingleChoiceQuestion extends QuestionBase {
  type: "single-choice";
  options: { id: number; text: string }[];
}

export interface MultipleChoiceQuestion extends QuestionBase {
  type: "multiple-choice";
  options: { id: number; text: string }[];
}

export type Question =
  | ReferenceQuestion
  | InputQuestion
  | SingleChoiceQuestion
  | MultipleChoiceQuestion;
